package br.com.api_food.useCases.customer;

import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.persistence.customer.CustomerPersistence;
import br.com.api_food.domain.useCases.customer.FindCustomerById;
import br.com.api_food.domain.useCases.store.FindStoreById;
import br.com.api_food.useCases.customer.exceptions.CustomerNotFound;
import br.com.api_food.useCases.store.exceptions.StoreNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerImplTest {

    @Mock
    private CustomerPersistence customerPersistencePort;

    @Mock
    private FindCustomerById findCustomerById;

    @Mock
    private FindStoreById findStoreById;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UpdateCustomerImpl updateCustomer;

    @Test
    void should_UpdateCustomerSuccessfully() {
        // Arrange
        var storeId = UUID.randomUUID();
        var customerId = UUID.randomUUID();

        var customerDomain = new CustomerDomain(
                "John Test",
                "john@mail.com",
                "12345678900",
                storeId,
                new StoreDomain());
        customerDomain.setId(customerId);

        var updatedCustomer = new CustomerDomain(
                "John Updated",
                "updated@mail.com",
                "00987654321",
                storeId,
                new StoreDomain());
        updatedCustomer.setId(customerId);

        when(findCustomerById.execute(customerId)).thenReturn(customerDomain);

        doAnswer(invocation -> {
            CustomerDomain source = invocation.getArgument(0);
            CustomerDomain target = invocation.getArgument(1);
            target.setName(source.getName());
            target.setEmail(source.getEmail());
            target.setCpf(source.getCpf());
            return null;
        }).when(modelMapper).map(updatedCustomer, customerDomain);

        when(customerPersistencePort.save(customerDomain)).thenReturn(updatedCustomer);

        // Act
        CustomerDomain response = updateCustomer.execute(updatedCustomer);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(customerId);
        assertThat(response.getName()).isEqualTo("John Updated");
        assertThat(response.getEmail()).isEqualTo("updated@mail.com");
        assertThat(response.getCpf()).isEqualTo("00987654321");

        verify(findCustomerById).execute(customerId);
        verify(modelMapper).map(updatedCustomer, customerDomain);
        verify(customerPersistencePort).save(customerDomain);
    }

    @Test
    void should_ThrowException_WhenCustomerNotFound() {
        // Arrange
        var storeId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var updatedCustomer = new CustomerDomain(
                "John Updated",
                "updated@mail.com",
                "00987654321",
                storeId,
                new StoreDomain());
        updatedCustomer.setId(customerId);

        when(findCustomerById.execute(customerId)).thenThrow(CustomerNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> updateCustomer.execute(updatedCustomer))
                .isInstanceOf(CustomerNotFound.class);

        verify(findCustomerById).execute(customerId);
        verifyNoInteractions(modelMapper);
        verifyNoInteractions(customerPersistencePort);
    }

    @Test
    void should_ThrowException_WhenStoreNotFound() {
        // Arrange
        var storeId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var updatedCustomer = new CustomerDomain(
                "John Updated",
                "updated@mail.com",
                "00987654321",
                storeId,
                new StoreDomain());
        updatedCustomer.setId(customerId);

        when(findStoreById.execute(storeId)).thenThrow(StoreNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> updateCustomer.execute(updatedCustomer))
                .isInstanceOf(StoreNotFound.class);

        verify(findCustomerById).execute(customerId);
        verify(findStoreById).execute(storeId);
        verifyNoInteractions(modelMapper);
        verifyNoInteractions(customerPersistencePort);
    }

}