package br.com.api_food.useCases.customer;

import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.domain.persistence.customer.CustomerPersistence;
import br.com.api_food.useCases.customer.exceptions.CustomerNotFoundByCPF;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindCustomerByCPFImplTest {

    @Mock
    private CustomerPersistence customerPersistencePort;

    @InjectMocks
    private FindCustomerByCPFImpl findCustomerByCPF;

    @Test
    void should_ReturnCustomer_WhenCpfExists() {
        // Arrange
        var cpf = "12345678900";
        var customer = new CustomerDomain();
        customer.setCpf(cpf);

        when(customerPersistencePort.findByCpf(cpf)).thenReturn(Optional.of(customer));

        // Act
        CustomerDomain result = findCustomerByCPF.execute(cpf);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCpf()).isEqualTo(cpf);
        verify(customerPersistencePort).findByCpf(cpf);
    }

    @Test
    void should_ThrowException_WhenCpfDoesNotExist() {
        // Arrange
        String cpf = "12345678900";
        when(customerPersistencePort.findByCpf(cpf)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> findCustomerByCPF.execute(cpf))
                .isInstanceOf(CustomerNotFoundByCPF.class);

        verify(customerPersistencePort).findByCpf(cpf);
    }
}