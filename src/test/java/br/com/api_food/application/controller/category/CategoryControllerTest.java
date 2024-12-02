package br.com.api_food.application.controller.category;

import br.com.api_food.application.dtos.category.CategoryDTO;
import br.com.api_food.application.dtos.category.CategoryInputDTO;
import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.useCases.category.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private FindCategoryById findCategoryById;

    @Mock
    private FindAllCategories findAllCategories;

    @Mock
    private CreateNewCategory createNewCategory;

    @Mock
    private UpdateCategory updateCategory;

    @Mock
    private DeleteCategoryById deleteCategoryById;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void shouldReturnCategoryById_whenCategoryExists() {
        UUID id = UUID.fromString("bfd76acd-5acd-4234-9dad-519eba365836");
        CategoryDomain categoryDomain = new CategoryDomain();
        CategoryDTO categoryDTO = new CategoryDTO();

        when(findCategoryById.execute(id)).thenReturn(categoryDomain);
        when(modelMapper.map(categoryDomain, CategoryDTO.class)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDTO, response.getBody());
    }

    @Test
    void shouldReturnAllCategories_whenCategoriesExist() {
        CategoryDomain categoryDomain = new CategoryDomain();
        CategoryDTO categoryDTO = new CategoryDTO();

        when(findAllCategories.execute()).thenReturn(List.of(categoryDomain));
        when(modelMapper.map(categoryDomain, CategoryDTO.class)).thenReturn(categoryDTO);

        ResponseEntity<List<CategoryDTO>> response = categoryController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(categoryDTO, response.getBody().get(0));
    }

    @Test
    void shouldReturnNoContent_whenNoCategoriesExist() {
        when(findAllCategories.execute()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CategoryDTO>> response = categoryController.getAll();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldReturnCreated_whenCategoryIsCreated() {
        CategoryInputDTO inputDTO = new CategoryInputDTO();
        CategoryDomain categoryDomain = new CategoryDomain();
        CategoryDTO categoryDTO = new CategoryDTO();

        when(modelMapper.map(inputDTO, CategoryDomain.class)).thenReturn(categoryDomain);
        when(createNewCategory.execute(any(CategoryDomain.class))).thenReturn(categoryDomain);
        when(modelMapper.map(categoryDomain, CategoryDTO.class)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.save(inputDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(categoryDTO, response.getBody());
    }

    @Test
    void shouldReturnUpdatedCategory_whenUpdateIsSuccessful() {
        UUID id = UUID.randomUUID();
        CategoryInputDTO inputDTO = new CategoryInputDTO();
        CategoryDomain categoryDomain = new CategoryDomain();
        CategoryDTO categoryDTO = new CategoryDTO();

        when(modelMapper.map(inputDTO, CategoryDomain.class)).thenReturn(categoryDomain);
        when(updateCategory.execute(any(CategoryDomain.class))).thenReturn(categoryDomain);
        when(modelMapper.map(categoryDomain, CategoryDTO.class)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.update(id, inputDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDTO, response.getBody());
    }

    @Test
    void shouldReturnNoContent_whenCategoryIsDeleted() {
        UUID id = UUID.randomUUID();

        doNothing().when(deleteCategoryById).execute(id);

        ResponseEntity<Void> response = categoryController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}