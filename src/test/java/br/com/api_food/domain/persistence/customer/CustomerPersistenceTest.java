package br.com.api_food.domain.persistence.customer;

import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.infra.persistence.entities.customer.CustomerEntity;
import br.com.api_food.infra.persistence.repositories.customer.CustomerJpaRepository;
import br.com.api_food.infra.persistence.repositories.customer.CustomerPersistencePortImpl;
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
class CustomerPersistenceTest {

    @Mock
    private CustomerJpaRepository customerJpaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerPersistencePortImpl customerPersistencePort;

    private UUID sampleId;
    private String sampleCpf;
    private CustomerDomain sampleDomain;
    private CustomerEntity sampleEntity;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleCpf = "12345678900";
        sampleDomain = new CustomerDomain();
        sampleEntity = new CustomerEntity();
    }

    @Test
    void testFindById_ReturnsCustomerDomain_WhenEntityExists() {
        when(customerJpaRepository.findById(sampleId)).thenReturn(Optional.of(sampleEntity));
        when(modelMapper.map(sampleEntity, CustomerDomain.class)).thenReturn(sampleDomain);

        Optional<CustomerDomain> result = customerPersistencePort.findById(sampleId);

        assertTrue(result.isPresent());
        assertEquals(sampleDomain, result.get());
        verify(customerJpaRepository).findById(sampleId);
        verify(modelMapper).map(sampleEntity, CustomerDomain.class);
    }

    @Test
    void testFindById_ReturnsEmptyOptional_WhenEntityDoesNotExist() {
        when(customerJpaRepository.findById(sampleId)).thenReturn(Optional.empty());

        Optional<CustomerDomain> result = customerPersistencePort.findById(sampleId);

        assertFalse(result.isPresent());
        verify(customerJpaRepository).findById(sampleId);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testSave_ReturnsSavedCustomerDomain() {
        when(modelMapper.map(sampleDomain, CustomerEntity.class)).thenReturn(sampleEntity);
        when(customerJpaRepository.save(sampleEntity)).thenReturn(sampleEntity);
        when(modelMapper.map(sampleEntity, CustomerDomain.class)).thenReturn(sampleDomain);

        CustomerDomain result = customerPersistencePort.save(sampleDomain);

        assertNotNull(result);
        assertEquals(sampleDomain, result);
        verify(modelMapper).map(sampleDomain, CustomerEntity.class);
        verify(customerJpaRepository).save(sampleEntity);
        verify(modelMapper).map(sampleEntity, CustomerDomain.class);
    }

    @Test
    void testFindAll_ReturnsListOfCustomerDomains_WhenEntitiesExist() {
        List<CustomerEntity> entities = List.of(sampleEntity);
        List<CustomerDomain> domains = List.of(sampleDomain);

        when(customerJpaRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(sampleEntity, CustomerDomain.class)).thenReturn(sampleDomain);

        List<CustomerDomain> result = customerPersistencePort.findAll();

        assertNotNull(result);
        assertEquals(domains.size(), result.size());
        assertEquals(domains, result);
        verify(customerJpaRepository).findAll();
        verify(modelMapper, times(entities.size())).map(sampleEntity, CustomerDomain.class);
    }

    @Test
    void testFindAll_ReturnsEmptyList_WhenNoEntitiesExist() {
        when(customerJpaRepository.findAll()).thenReturn(List.of());

        List<CustomerDomain> result = customerPersistencePort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerJpaRepository).findAll();
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testFindByCpf_ReturnsCustomerDomain_WhenEntityExists() {
        when(customerJpaRepository.findByCpf(sampleCpf)).thenReturn(Optional.of(sampleEntity));
        when(modelMapper.map(sampleEntity, CustomerDomain.class)).thenReturn(sampleDomain);

        Optional<CustomerDomain> result = customerPersistencePort.findByCpf(sampleCpf);

        assertTrue(result.isPresent());
        assertEquals(sampleDomain, result.get());
        verify(customerJpaRepository).findByCpf(sampleCpf);
        verify(modelMapper).map(sampleEntity, CustomerDomain.class);
    }

    @Test
    void testFindByCpf_ReturnsEmptyOptional_WhenEntityDoesNotExist() {
        when(customerJpaRepository.findByCpf(sampleCpf)).thenReturn(Optional.empty());

        Optional<CustomerDomain> result = customerPersistencePort.findByCpf(sampleCpf);

        assertFalse(result.isPresent());
        verify(customerJpaRepository).findByCpf(sampleCpf);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void testDeleteById_CallsRepositoryDeleteById() {
        doNothing().when(customerJpaRepository).deleteById(sampleId);

        customerPersistencePort.deleteByID(sampleId);

        verify(customerJpaRepository).deleteById(sampleId);
    }

}