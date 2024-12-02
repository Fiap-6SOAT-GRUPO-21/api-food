package br.com.api_food.useCases.category;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.persistence.category.CategoryPersistence;
import br.com.api_food.domain.useCases.category.FindCategoryById;
import br.com.api_food.domain.useCases.product.FindAllByCategoryId;
import br.com.api_food.useCases.category.exceptions.CategoryNotFound;
import br.com.api_food.useCases.category.exceptions.ExistProductInCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteCategoryByIdImplTest {

    @Mock
    private FindCategoryById findCategoryById;

    @Mock
    private FindAllByCategoryId findAllByCategoryId;

    @Mock
    private CategoryPersistence categoryPersistence;

    @InjectMocks
    private DeleteCategoryByIdImpl deleteCategoryById;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_DeleteCategory_WhenNoProductsExistInCategory() {

        // Arrange
        var categoryId = UUID.randomUUID();
        CategoryDomain categoryDomain = new CategoryDomain();
        categoryDomain.setId(categoryId);

        when(findCategoryById.execute(categoryId)).thenReturn(categoryDomain);
        when(findAllByCategoryId.execute(categoryId)).thenReturn(List.of());

        // Act
        deleteCategoryById.execute(categoryId);

        // Assert
        verify(findCategoryById).execute(categoryId);
        verify(findAllByCategoryId).execute(categoryId);
        verify(categoryPersistence).deleteByID(categoryId);
    }

    @Test
    void should_ThrowException_WhenCategoryNotFound() {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        when(findCategoryById.execute(categoryId)).thenThrow(CategoryNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> deleteCategoryById.execute(categoryId))
                .isInstanceOf(CategoryNotFound.class);

        verify(findCategoryById).execute(categoryId);
        verifyNoInteractions(findAllByCategoryId);
        verify(categoryPersistence, never()).deleteByID(any());
    }

    @Test
    void should_ThrowException_WhenProductsExistInCategory() {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        CategoryDomain categoryDomain = new CategoryDomain();
        categoryDomain.setId(categoryId);

        ProductDomain product1 = new ProductDomain();
        product1.setId(UUID.randomUUID());
        product1.setName("Product 1");

        ProductDomain product2 = new ProductDomain();
        product2.setId(UUID.randomUUID());
        product2.setName("Product 2");

        List<ProductDomain> products = List.of(product1, product2);

        when(findCategoryById.execute(categoryId)).thenReturn(categoryDomain);
        when(findAllByCategoryId.execute(categoryId)).thenReturn(products);

        // Act & Assert
        assertThatThrownBy(() -> deleteCategoryById.execute(categoryId))
                .isInstanceOf(ExistProductInCategory.class)
                .hasMessageContaining("Exist product in category")
                .hasMessageContaining("id: " + product1.getId() + ", name: " + product1.getName())
                .hasMessageContaining("id: " + product2.getId() + ", name: " + product2.getName());

        verify(findCategoryById).execute(categoryId);
        verify(findAllByCategoryId).execute(categoryId);
        verify(categoryPersistence, never()).deleteByID(any());
    }

}