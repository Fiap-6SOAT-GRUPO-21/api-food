package br.com.api_food.useCases.product;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.persistence.product.ProductPersistence;
import br.com.api_food.useCases.product.exceptions.ProductNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindProductByIdAndIdStoreImplTest {

    @Mock
    private ProductPersistence productPersistence;

    @InjectMocks
    private FindProductByIdAndIdStoreImpl findProductByIdAndIdStore;

    private UUID idProduct;
    private UUID idStore;
    private ProductDomain productDomain;

    @BeforeEach
    void setUp() {

        idProduct = UUID.randomUUID();
        idStore = UUID.randomUUID();

        productDomain = new ProductDomain(
                idProduct,
                "Product Name",
                BigDecimal.valueOf(10.00),
                idStore,
                new StoreDomain(),
                UUID.randomUUID(),
                new CategoryDomain());
    }

    @Test
    void should_ReturnProduct_WhenFound() {
        // Arrange
        when(productPersistence.findByIdAndIdStore(idProduct, idStore)).thenReturn(Optional.of(productDomain));

        // Act
        ProductDomain response = findProductByIdAndIdStore.execute(idProduct, idStore);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(productDomain);

        verify(productPersistence, times(1)).findByIdAndIdStore(idProduct, idStore);
        verifyNoMoreInteractions(productPersistence);
    }

    @Test
    void should_ThrowException_WhenProductNotFound() {
        // Arrange
        when(productPersistence.findByIdAndIdStore(idProduct, idStore)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> findProductByIdAndIdStore.execute(idProduct, idStore))
                .isInstanceOf(ProductNotFound.class)
                .hasMessage("Product with id " + idProduct + ", by idStore: " + idStore + " not found");

        verify(productPersistence).findByIdAndIdStore(idProduct, idStore);
    }
}