package br.com.api_food.useCases.store;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.entity.store.payment.MercadoPagoGatewayDomain;
import br.com.api_food.domain.persistence.store.StorePersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateNewStoreImplTest {

    @Mock
    private StorePersistence storePersistence;

    @InjectMocks
    private CreateNewStoreImpl createNewStore;

    private StoreDomain storeDomain;

    @BeforeEach
    void setUp() {

        var mercadoPagoDomain = new MercadoPagoGatewayDomain("Collectors", "externalPos");
        var storeId = UUID.fromString("4b327e9a-9371-4172-8630-1bb92dbf9dfc");
        storeDomain = new StoreDomain(storeId, "Store Name", true, UUID.randomUUID(), mercadoPagoDomain);
    }

    @Test
    void should_SaveStoreSuccessfully() {

        // Arrange
        when(storePersistence.save(storeDomain)).thenReturn(storeDomain);

        // Act
        var response = createNewStore.execute(storeDomain);

        // Assert
        verify(storePersistence).save(storeDomain);

        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(storeDomain);
    }

}