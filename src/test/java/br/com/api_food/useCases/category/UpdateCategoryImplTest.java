package br.com.api_food.useCases.category;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.useCases.category.CreateNewCategory;
import br.com.api_food.domain.useCases.category.FindCategoryById;
import br.com.api_food.useCases.category.exceptions.CategoryNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UpdateCategoryImplTest {

    @Mock
    private FindCategoryById findCategoryById;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CreateNewCategory createNewCategory;

    @InjectMocks
    private UpdateCategoryImpl updateCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_UpdateCategorySuccessfully() {
        // Arrange
        var categoryId = UUID.randomUUID();
        var oldCategory = new CategoryDomain();
        oldCategory.setId(categoryId);
        oldCategory.setName("Old Category Name");

        var updatedCategory = new CategoryDomain();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("New Category Name");

        when(findCategoryById.execute(categoryId)).thenReturn(oldCategory);
        doAnswer(invocation -> {
            CategoryDomain source = invocation.getArgument(0);
            CategoryDomain target = invocation.getArgument(1);
            target.setName(source.getName());
            return null;
        }).when(modelMapper).map(updatedCategory, oldCategory);

        when(createNewCategory.execute(oldCategory)).thenReturn(updatedCategory);

        // Act
        var response = updateCategory.execute(updatedCategory);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(categoryId);
        assertThat(response.getName()).isEqualTo("New Category Name");

        verify(findCategoryById).execute(categoryId);
        verify(modelMapper).map(updatedCategory, oldCategory);
        verify(createNewCategory).execute(oldCategory);
    }

    @Test
    void should_ThrowException_WhenCategoryNotFound() {
        // Arrange
        var categoryId = UUID.randomUUID();
        CategoryDomain updatedCategory = new CategoryDomain();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("New Category Name");

        when(findCategoryById.execute(categoryId)).thenThrow(CategoryNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> updateCategory.execute(updatedCategory))
                .isInstanceOf(CategoryNotFound.class);

        verify(findCategoryById).execute(categoryId);
        verifyNoInteractions(modelMapper);
        verifyNoInteractions(createNewCategory);
    }
}