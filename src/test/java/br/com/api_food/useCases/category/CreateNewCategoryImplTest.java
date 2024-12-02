package br.com.api_food.useCases.category;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.entity.store.payment.MercadoPagoGatewayDomain;
import br.com.api_food.domain.persistence.category.CategoryPersistence;
import br.com.api_food.domain.useCases.store.FindStoreById;
import br.com.api_food.useCases.store.exceptions.StoreNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreateNewCategoryImplTest {

    @Mock
    private FindStoreById findStoreById;

    @Mock
    private CategoryPersistence categoryPersistence;

    @InjectMocks
    private CreateNewCategoryImpl useCase;

    private CategoryDomain categoryDomain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        var mercadoPagoDomain = new MercadoPagoGatewayDomain("Collectors", "externalPos");
        var storeId = UUID.fromString("4b327e9a-9371-4172-8630-1bb92dbf9dfc");
        var store = new StoreDomain(storeId, "Store Name", true, UUID.randomUUID(), mercadoPagoDomain);
        categoryDomain = new CategoryDomain("Category", storeId, store);
    }

    @Test
    void should_SaveCategory_WhenStoreExists() {

        // Arrange
        when(categoryPersistence.save(categoryDomain)).thenReturn(categoryDomain);

        // Act
        var response = useCase.execute(categoryDomain);

        // Assert
        verify(findStoreById).execute(categoryDomain.getIdStore());
        verify(categoryPersistence).save(categoryDomain);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Category");
    }

    @Test
    void should_ThrowException_WhenStoreDoesNotExist() {

        // Arrange
        when(findStoreById.execute(categoryDomain.getIdStore())).thenThrow(StoreNotFound.class);

        // Act & Assert
        verify(categoryPersistence, never()).save(any());

        assertThatThrownBy(() -> useCase.execute(categoryDomain))
                .isInstanceOf(StoreNotFound.class);
    }
}
