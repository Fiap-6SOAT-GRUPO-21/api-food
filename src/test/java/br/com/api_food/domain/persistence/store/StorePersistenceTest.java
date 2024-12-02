package br.com.api_food.domain.persistence.store;

import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.infra.persistence.entities.store.StoreEntity;
import br.com.api_food.infra.persistence.entities.store.payment.MercadoPagoGatewayEntity;
import br.com.api_food.infra.persistence.repositories.store.StoreJpaRepository;
import br.com.api_food.infra.persistence.repositories.store.StorePersistencePortImpl;
import br.com.api_food.useCases.store.exceptions.StoreNotFound;
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
class StorePersistenceTest {

    @Mock
    private StoreJpaRepository storeJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StorePersistencePortImpl storePersistencePort;

    private UUID sampleId;
    private StoreDomain sampleDomain;
    private StoreEntity sampleEntity;
    private StoreEntity savedEntity;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleDomain = new StoreDomain();
        sampleEntity = new StoreEntity();
        savedEntity = new StoreEntity();
    }

    @Test
    void testSave_ReturnsSavedStoreDomain() {
        MercadoPagoGatewayEntity mockedGatewayEntity = mock(MercadoPagoGatewayEntity.class);
        UUID uuid = UUID.fromString("7d4047f9-534f-4d27-a1c9-3117c6c17eb9");
        when(mockedGatewayEntity.getId()).thenReturn(uuid);

        savedEntity.setMercadoPagoGateway(mockedGatewayEntity);

        when(modelMapper.map(sampleDomain, StoreEntity.class)).thenReturn(sampleEntity);
        when(storeJpaRepository.save(sampleEntity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, StoreDomain.class)).thenReturn(sampleDomain);

        StoreDomain result = storePersistencePort.save(sampleDomain);

        assertNotNull(result);
        assertEquals(sampleDomain, result);

        verify(modelMapper).map(sampleDomain, StoreEntity.class);
        verify(storeJpaRepository, times(2)).save(any(StoreEntity.class));
        verify(modelMapper).map(savedEntity, StoreDomain.class);
    }

    @Test
    void testFindById_ReturnsStoreDomain_WhenEntityExists() {
        when(storeJpaRepository.findById(sampleId)).thenReturn(Optional.of(sampleEntity));
        when(modelMapper.map(sampleEntity, StoreDomain.class)).thenReturn(sampleDomain);

        Optional<StoreDomain> result = storePersistencePort.findById(sampleId);

        assertTrue(result.isPresent());
        assertEquals(sampleDomain, result.get());
        verify(storeJpaRepository).findById(sampleId);
        verify(modelMapper).map(sampleEntity, StoreDomain.class);
    }

    @Test
    void testFindById_ReturnsEmptyOptional_WhenEntityDoesNotExist() {
        when(storeJpaRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<StoreDomain> result = storePersistencePort.findById(sampleId);

        assertFalse(result.isPresent());
        verify(storeJpaRepository).findById(sampleId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testFindAll_ReturnsListOfStoreDomains_WhenEntitiesExist() {
        List<StoreEntity> entities = List.of(sampleEntity);
        List<StoreDomain> domains = List.of(sampleDomain);

        when(storeJpaRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(sampleEntity, StoreDomain.class)).thenReturn(sampleDomain);

        List<StoreDomain> result = storePersistencePort.findAll();

        assertNotNull(result);
        assertEquals(domains.size(), result.size());
        assertEquals(domains, result);
        verify(storeJpaRepository).findAll();
        verify(modelMapper, times(entities.size())).map(sampleEntity, StoreDomain.class);
    }

    @Test
    void testFindAll_ReturnsEmptyList_WhenNoEntitiesExist() {
        when(storeJpaRepository.findAll()).thenReturn(List.of());

        List<StoreDomain> result = storePersistencePort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(storeJpaRepository).findAll();
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testEntityFindById_ReturnsStoreEntity_WhenEntityExists() {
        when(storeJpaRepository.findById(sampleId)).thenReturn(Optional.of(sampleEntity));

        StoreEntity result = storePersistencePort.entityfindById(sampleId);

        assertNotNull(result);
        assertEquals(sampleEntity, result);
        verify(storeJpaRepository).findById(sampleId);
    }

    @Test
    void testEntityFindById_ThrowsStoreNotFound_WhenEntityDoesNotExist() {
        when(storeJpaRepository.findById(sampleId)).thenReturn(Optional.empty());

        assertThrows(StoreNotFound.class, () -> storePersistencePort.entityfindById(sampleId));

        verify(storeJpaRepository).findById(sampleId);
    }

}