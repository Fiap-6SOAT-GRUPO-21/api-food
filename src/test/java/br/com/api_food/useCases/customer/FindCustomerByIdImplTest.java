package br.com.api_food.useCases.customer;

import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.domain.persistence.customer.CustomerPersistence;
import br.com.api_food.useCases.customer.exceptions.CustomerNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCustomerByIdImplTest {

    @Mock
    private CustomerPersistence customerPersistencePort;

    @InjectMocks
    private FindCustomerByIdImpl findCustomerById;

    @Test
    void should_ReturnCustomer_WhenIdExists() {
        // Arrange
        var id = UUID.randomUUID();
        var customer = new CustomerDomain();
        customer.setId(id);

        when(customerPersistencePort.findById(id)).thenReturn(Optional.of(customer));

        // Act
        CustomerDomain result = findCustomerById.execute(id);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(customerPersistencePort).findById(id);
    }

    @Test
    void should_ThrowException_WhenIdDoesNotExist() {
        // Arrange
        var id = UUID.randomUUID();
        when(customerPersistencePort.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> findCustomerById.execute(id))
                .isInstanceOf(CustomerNotFound.class);

        verify(customerPersistencePort).findById(id);
    }
}