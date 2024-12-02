package br.com.api_food.useCases.customer;

import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.persistence.customer.CustomerPersistence;
import br.com.api_food.domain.useCases.store.FindStoreById;
import br.com.api_food.useCases.customer.exceptions.ExistCustomer;
import br.com.api_food.useCases.store.exceptions.StoreNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateNewCustomerImplTest {

    @Mock
    private CustomerPersistence customerPersistence;

    @Mock
    private FindStoreById findStoreById;

    @InjectMocks
    private CreateNewCustomerImpl createNewCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_CreateNewCustomerSuccessfully() {
        // Arrange
        var storeId = UUID.randomUUID();
        String cpf = "12345678900";
        var store = new StoreDomain();

        var customerDomain = new CustomerDomain(
                "John Test",
                "john@mail.com",
                cpf,
                storeId,
                store);

        when(customerPersistence.findByCpf(cpf)).thenReturn(Optional.empty());
        when(customerPersistence.save(any(CustomerDomain.class))).thenReturn(customerDomain);

        // Act
        var response = createNewCustomer.execute(customerDomain);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getCpf()).isEqualTo(cpf);
        assertThat(response.getName()).isEqualTo("John Test");
        assertThat(response.getEmail()).isEqualTo("john@mail.com");

        verify(customerPersistence).findByCpf(cpf);
        verify(findStoreById).execute(storeId);
        verify(customerPersistence).save(customerDomain);
    }

    @Test
    void should_ThrowException_WhenCustomerAlreadyExists() {
        // Arrange
        var cpf = "12345678900";

        var existingCustomer = new CustomerDomain();
        existingCustomer.setCpf(cpf);

        CustomerDomain customerDomain = new CustomerDomain();
        customerDomain.setCpf(cpf);

        when(customerPersistence.findByCpf(cpf)).thenReturn(Optional.of(existingCustomer));

        // Act & Assert
        assertThatThrownBy(() -> createNewCustomer.execute(customerDomain))
                .isInstanceOf(ExistCustomer.class);

        verify(customerPersistence).findByCpf(cpf);
        verifyNoInteractions(findStoreById);
        verifyNoMoreInteractions(customerPersistence);
    }

    @Test
    void should_ThrowException_WhenStoreNotFound() {

        // Arrange
        var storeId = UUID.randomUUID();
        var cpf = "12345678900";

        var customerDomain = new CustomerDomain();
        customerDomain.setCpf(cpf);
        customerDomain.setIdStore(storeId);

        when(customerPersistence.findByCpf(cpf)).thenReturn(Optional.empty());
        when(findStoreById.execute(storeId)).thenThrow(StoreNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> createNewCustomer.execute(customerDomain))
                .isInstanceOf(StoreNotFound.class);

        verify(customerPersistence).findByCpf(cpf);
        verify(findStoreById).execute(storeId);
        verifyNoMoreInteractions(customerPersistence);
    }
}