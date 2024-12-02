package br.com.api_food.useCases.category;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.persistence.category.CategoryPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindAllCategoriesImplTest {

    @Mock
    private CategoryPersistence categoryPersistence;

    @InjectMocks
    private FindAllCategoriesImpl findAllCategories;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_ReturnAllCategories() {
        // Arrange
        var category1 = new CategoryDomain();
        category1.setId(UUID.randomUUID());
        category1.setName("Category 1");

        var category2 = new CategoryDomain();
        category2.setId(UUID.randomUUID());
        category2.setName("Category 2");

        when(categoryPersistence.findAll()).thenReturn(List.of(category1, category2));

        // Act
        var response = findAllCategories.execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response).containsExactlyInAnyOrder(category1, category2);
        verify(categoryPersistence).findAll();
    }

    @Test
    void should_ReturnEmptyList_WhenNoCategoriesExist() {
        // Arrange
        when(categoryPersistence.findAll()).thenReturn(List.of());

        // Act
        List<CategoryDomain> result = findAllCategories.execute();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(categoryPersistence).findAll();
    }
}