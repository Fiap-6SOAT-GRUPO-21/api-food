package br.com.api_food.useCases.customer;

import br.com.api_food.domain.persistence.customer.CustomerPersistence;
import br.com.api_food.domain.useCases.customer.FindCustomerById;
import br.com.api_food.useCases.customer.exceptions.CustomerNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class DeleteCustomerByIdImplTest {

    @Mock
    private CustomerPersistence customerPersistence;

    @Mock
    private FindCustomerById findCustomerById;

    @InjectMocks
    private DeleteCustomerByIdImpl deleteCustomerById;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_DeleteCustomerSuccessfully() {
        // Arrange
        var customerId = UUID.randomUUID();

        doNothing().when(customerPersistence).deleteByID(customerId);

        // Act
        deleteCustomerById.execute(customerId);

        // Assert
        verify(findCustomerById).execute(customerId);
        verify(customerPersistence).deleteByID(customerId);
        verifyNoMoreInteractions(findCustomerById, customerPersistence);
    }

    @Test
    void should_ThrowException_WhenCustomerNotFound() {

        // Arrange
        var customerId = UUID.randomUUID();
        when(findCustomerById.execute(customerId)).thenThrow(CustomerNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> deleteCustomerById.execute(customerId))
                .isInstanceOf(CustomerNotFound.class);

        verify(findCustomerById).execute(customerId);
        verifyNoInteractions(customerPersistence);
    }
}