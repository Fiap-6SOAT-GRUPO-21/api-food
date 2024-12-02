package br.com.api_food.domain.persistence.product;

import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.infra.persistence.entities.product.ProductEntity;
import br.com.api_food.infra.persistence.repositories.product.ProductJpaRepository;
import br.com.api_food.infra.persistence.repositories.product.ProductPersistencePortImpl;
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
class ProductPersistenceTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductPersistencePortImpl productPersistencePort;

    private UUID sampleIdProduct;
    private UUID sampleIdStore;
    private UUID sampleIdCategory;
    private ProductDomain sampleDomain;
    private ProductEntity sampleEntity;

    @BeforeEach
    void setUp() {
        sampleIdProduct = UUID.randomUUID();
        sampleIdStore = UUID.randomUUID();
        sampleIdCategory = UUID.randomUUID();
        sampleDomain = new ProductDomain();
        sampleEntity = new ProductEntity();
    }

    @Test
    void testFindById_ReturnsProductDomain_WhenEntityExists() {
        when(productJpaRepository.findById(sampleIdProduct)).thenReturn(Optional.of(sampleEntity));
        when(modelMapper.map(sampleEntity, ProductDomain.class)).thenReturn(sampleDomain);

        Optional<ProductDomain> result = productPersistencePort.findById(sampleIdProduct);

        assertTrue(result.isPresent());
        assertEquals(sampleDomain, result.get());
        verify(productJpaRepository).findById(sampleIdProduct);
        verify(modelMapper).map(sampleEntity, ProductDomain.class);
    }

    @Test
    void testFindById_ReturnsEmptyOptional_WhenEntityDoesNotExist() {
        when(productJpaRepository.findById(sampleIdProduct)).thenReturn(Optional.empty());

        Optional<ProductDomain> result = productPersistencePort.findById(sampleIdProduct);

        assertFalse(result.isPresent());
        verify(productJpaRepository).findById(sampleIdProduct);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testFindByIdAndIdStore_ReturnsProductDomain_WhenEntityExists() {
        when(productJpaRepository.findByIdAndIdStore(sampleIdProduct, sampleIdStore)).thenReturn(Optional.of(sampleEntity));
        when(modelMapper.map(sampleEntity, ProductDomain.class)).thenReturn(sampleDomain);

        Optional<ProductDomain> result = productPersistencePort.findByIdAndIdStore(sampleIdProduct, sampleIdStore);

        assertTrue(result.isPresent());
        assertEquals(sampleDomain, result.get());
        verify(productJpaRepository).findByIdAndIdStore(sampleIdProduct, sampleIdStore);
        verify(modelMapper).map(sampleEntity, ProductDomain.class);
    }

    @Test
    void testFindByIdAndIdStore_ReturnsEmptyOptional_WhenEntityDoesNotExist() {
        when(productJpaRepository.findByIdAndIdStore(sampleIdProduct, sampleIdStore)).thenReturn(Optional.empty());

        Optional<ProductDomain> result = productPersistencePort.findByIdAndIdStore(sampleIdProduct, sampleIdStore);

        assertFalse(result.isPresent());
        verify(productJpaRepository).findByIdAndIdStore(sampleIdProduct, sampleIdStore);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testSave_ReturnsSavedProductDomain() {
        when(modelMapper.map(sampleDomain, ProductEntity.class)).thenReturn(sampleEntity);
        when(productJpaRepository.save(sampleEntity)).thenReturn(sampleEntity);
        when(modelMapper.map(sampleEntity, ProductDomain.class)).thenReturn(sampleDomain);

        ProductDomain result = productPersistencePort.save(sampleDomain);

        assertNotNull(result);
        assertEquals(sampleDomain, result);
        verify(modelMapper).map(sampleDomain, ProductEntity.class);
        verify(productJpaRepository).save(sampleEntity);
        verify(modelMapper).map(sampleEntity, ProductDomain.class);
    }

    @Test
    void testDeleteById_CallsRepositoryDeleteById() {
        doNothing().when(productJpaRepository).deleteById(sampleIdProduct);

        productPersistencePort.deleteByID(sampleIdProduct);

        verify(productJpaRepository).deleteById(sampleIdProduct);
    }

    @Test
    void testFindAll_ReturnsListOfProductDomains_WhenEntitiesExist() {
        List<ProductEntity> entities = List.of(sampleEntity);
        List<ProductDomain> domains = List.of(sampleDomain);

        when(productJpaRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(sampleEntity, ProductDomain.class)).thenReturn(sampleDomain);

        List<ProductDomain> result = productPersistencePort.findAll();

        assertNotNull(result);
        assertEquals(domains.size(), result.size());
        assertEquals(domains, result);
        verify(productJpaRepository).findAll();
        verify(modelMapper, times(entities.size())).map(sampleEntity, ProductDomain.class);
    }

    @Test
    void testFindAll_ReturnsEmptyList_WhenNoEntitiesExist() {
        when(productJpaRepository.findAll()).thenReturn(List.of());

        List<ProductDomain> result = productPersistencePort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productJpaRepository).findAll();
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testFindAllByCategory_ReturnsListOfProductDomains_WhenEntitiesExist() {
        List<ProductEntity> entities = List.of(sampleEntity);
        List<ProductDomain> domains = List.of(sampleDomain);

        when(productJpaRepository.findAllByIdCategory(sampleIdCategory)).thenReturn(entities);
        when(modelMapper.map(sampleEntity, ProductDomain.class)).thenReturn(sampleDomain);

        List<ProductDomain> result = productPersistencePort.findAllByCategory(sampleIdCategory);

        assertNotNull(result);
        assertEquals(domains.size(), result.size());
        assertEquals(domains, result);
        verify(productJpaRepository).findAllByIdCategory(sampleIdCategory);
        verify(modelMapper, times(entities.size())).map(sampleEntity, ProductDomain.class);
    }

    @Test
    void testFindAllByCategory_ReturnsEmptyList_WhenNoEntitiesExist() {
        when(productJpaRepository.findAllByIdCategory(sampleIdCategory)).thenReturn(List.of());

        List<ProductDomain> result = productPersistencePort.findAllByCategory(sampleIdCategory);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productJpaRepository).findAllByIdCategory(sampleIdCategory);
        verifyNoInteractions(modelMapper);
    }

}