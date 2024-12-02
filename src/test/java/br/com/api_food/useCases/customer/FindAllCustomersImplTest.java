package br.com.api_food.useCases.customer;

import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.persistence.customer.CustomerPersistence;
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

class FindAllCustomersImplTest {

    @Mock
    private CustomerPersistence customerPersistencePort;

    @InjectMocks
    private FindAllCustomersImpl findAllCustomers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_ReturnAllCustomers() {

        // Arrange
        var storeId = UUID.randomUUID();
        var store = new StoreDomain();

        var customer1 = new CustomerDomain(
                "John Test",
                "john@mail.com",
                "12345678900",
                storeId,
                store);

        var customer2 = new CustomerDomain(
                "Michael Test",
                "michael@mail.com",
                "12345678900",
                storeId,
                store);

        var customer3 = new CustomerDomain(
                "Thomas Test",
                "thomas@mail.com",
                "12345678900",
                storeId,
                store);

        when(customerPersistencePort.findAll()).thenReturn(List.of(customer1, customer2, customer3));

        // Act
        List<CustomerDomain> response = findAllCustomers.execute();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(3);
        assertThat(response).containsExactlyInAnyOrder(customer1, customer2, customer3);
        verify(customerPersistencePort).findAll();
    }

    @Test
    void should_ReturnEmptyList_WhenNoCustomersExist() {
        // Arrange
        when(customerPersistencePort.findAll()).thenReturn(List.of());

        // Act
        List<CustomerDomain> result = findAllCustomers.execute();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(customerPersistencePort).findAll();
    }
}