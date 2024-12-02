package br.com.api_food.useCases.product;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.persistence.product.ProductPersistence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllProductsImplTest {

    @Mock
    private ProductPersistence productPersistence;

    @InjectMocks
    private FindAllProductsImpl findAllProducts;

    @Test
    void should_ReturnAllProduct() {

        // Arrange
        var product1 = new ProductDomain(UUID.randomUUID(),
                "Product 1",
                BigDecimal.valueOf(10.00),
                UUID.randomUUID(),
                new StoreDomain(),
                UUID.randomUUID(),
                new CategoryDomain());
        var product2 = new ProductDomain(UUID.randomUUID(),
                "Product 2",
                BigDecimal.valueOf(10.00),
                UUID.randomUUID(),
                new StoreDomain(),
                UUID.randomUUID(),
                new CategoryDomain());

        when(productPersistence.findAll()).thenReturn(List.of(product1, product2));

        // Act
        List<ProductDomain> response = findAllProducts.execute();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response).containsExactlyInAnyOrder(product1, product2);
        verify(productPersistence).findAll();
    }

    @Test
    void should_ReturnEmptyList_WhenNoProductsExist() {
        // Arrange
        when(productPersistence.findAll()).thenReturn(List.of());

        // Act
        List<ProductDomain> response = findAllProducts.execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
        verify(productPersistence).findAll();
    }

}