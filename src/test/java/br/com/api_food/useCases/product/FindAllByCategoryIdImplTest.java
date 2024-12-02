package br.com.api_food.useCases.product;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.persistence.product.ProductPersistence;
import br.com.api_food.domain.useCases.category.FindCategoryById;
import br.com.api_food.useCases.category.exceptions.CategoryNotFound;
import br.com.api_food.useCases.customer.exceptions.CustomerNotFoundByCPF;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllByCategoryIdImplTest {

    @Mock
    private FindCategoryById findCategoryById;

    @Mock
    private ProductPersistence productPersistence;

    @InjectMocks
    private FindAllByCategoryIdImpl findAllByCategoryId;

    @Test
    void should_ReturnAllProducts_WhenCategoryIdExists() {
        // Arrange
        var categoryId = UUID.randomUUID();
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
        when(productPersistence.findAllByCategory(categoryId)).thenReturn(List.of(product1, product2));

        // Act
        List<ProductDomain> response = findAllByCategoryId.execute(categoryId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response).containsExactlyInAnyOrder(product1, product2);
        verify(productPersistence).findAllByCategory(categoryId);
    }

    @Test
    void should_ThrowException_WhenCategoryIdDoesNotExist() {
        // Arrange
        var categoryId = UUID.randomUUID();
        when(findCategoryById.execute(categoryId)).thenThrow(CategoryNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> findAllByCategoryId.execute(categoryId))
                .isInstanceOf(CategoryNotFound.class);

        verifyNoInteractions(productPersistence);
    }

}