package br.com.api_food.useCases.category;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.persistence.category.CategoryPersistence;
import br.com.api_food.useCases.category.exceptions.CategoryNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindCategoryByIdImplTest {

    @Mock
    private CategoryPersistence categoryPersistence;

    @InjectMocks
    private FindCategoryByIdImpl findCategoryById;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_ReturnCategory_WhenIdExists() {

        // Arrange
        var categoryId = UUID.randomUUID();
        CategoryDomain category = new CategoryDomain();
        category.setId(categoryId);
        category.setName("Category Name");

        when(categoryPersistence.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        var response = findCategoryById.execute(categoryId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(categoryId);
        assertThat(response.getName()).isEqualTo("Category Name");
        verify(categoryPersistence).findById(categoryId);
    }

    @Test
    void should_ThrowException_WhenIdDoesNotExist() {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        when(categoryPersistence.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> findCategoryById.execute(categoryId))
                .isInstanceOf(CategoryNotFound.class)
                .hasMessage("Category not exists");

        verify(categoryPersistence).findById(categoryId);
    }

}