package br.com.api_food.domain.persistence.category;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.infra.persistence.entities.category.CategoryEntity;
import br.com.api_food.infra.persistence.repositories.category.CategoryJpaRepository;
import br.com.api_food.infra.persistence.repositories.category.CategoryPersistencePortImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryPersistenceTest {

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryPersistencePortImpl categoryPersistencePort;

    private UUID sampleId;
    private CategoryDomain sampleDomain;
    private CategoryEntity sampleEntity;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleDomain = new CategoryDomain();
        sampleEntity = new CategoryEntity();
    }

    @Test
    void testFindById_ReturnsCategoryDomain_WhenEntityExists() {
        when(categoryJpaRepository.findById(sampleId)).thenReturn(Optional.of(sampleEntity));
        when(modelMapper.map(sampleEntity, CategoryDomain.class)).thenReturn(sampleDomain);

        Optional<CategoryDomain> result = categoryPersistencePort.findById(sampleId);

        assertTrue(result.isPresent());
        assertEquals(sampleDomain, result.get());
        verify(categoryJpaRepository).findById(sampleId);
        verify(modelMapper).map(sampleEntity, CategoryDomain.class);
    }

    @Test
    void testFindById_ReturnsEmptyOptional_WhenEntityDoesNotExist() {
        when(categoryJpaRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<CategoryDomain> result = categoryPersistencePort.findById(sampleId);

        assertFalse(result.isPresent());
        verify(categoryJpaRepository).findById(sampleId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testSave_ReturnsSavedCategoryDomain() {
        when(modelMapper.map(sampleDomain, CategoryEntity.class)).thenReturn(sampleEntity);
        when(categoryJpaRepository.save(sampleEntity)).thenReturn(sampleEntity);
        when(modelMapper.map(sampleEntity, CategoryDomain.class)).thenReturn(sampleDomain);

        CategoryDomain result = categoryPersistencePort.save(sampleDomain);

        assertNotNull(result);
        assertEquals(sampleDomain, result);
        verify(modelMapper).map(sampleDomain, CategoryEntity.class);
        verify(categoryJpaRepository).save(sampleEntity);
        verify(modelMapper).map(sampleEntity, CategoryDomain.class);
    }

    @Test
    void testDeleteById_CallsRepositoryDeleteById() {
        doNothing().when(categoryJpaRepository).deleteById(sampleId);

        categoryPersistencePort.deleteByID(sampleId);

        verify(categoryJpaRepository).deleteById(sampleId);
    }

    @Test
    void testFindAll_ReturnsListOfCategoryDomains_WhenEntitiesExist() {
        List<CategoryEntity> entities = List.of(sampleEntity);
        List<CategoryDomain> domains = List.of(sampleDomain);

        when(categoryJpaRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(sampleEntity, CategoryDomain.class)).thenReturn(sampleDomain);

        List<CategoryDomain> result = categoryPersistencePort.findAll();

        assertNotNull(result);
        assertEquals(domains.size(), result.size());
        assertEquals(domains, result);
        verify(categoryJpaRepository).findAll();
        verify(modelMapper, times(entities.size())).map(sampleEntity, CategoryDomain.class);
    }

    @Test
    void testFindAll_ReturnsEmptyList_WhenNoEntitiesExist() {
        when(categoryJpaRepository.findAll()).thenReturn(List.of());

        List<CategoryDomain> result = categoryPersistencePort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryJpaRepository).findAll();
        verifyNoInteractions(modelMapper);
    }
}