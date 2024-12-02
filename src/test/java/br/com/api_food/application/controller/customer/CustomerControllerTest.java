package br.com.api_food.application.controller.customer;

import br.com.api_food.application.dtos.customer.CustomerDTO;
import br.com.api_food.application.dtos.customer.CustomerInputDTO;
import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.domain.useCases.customer.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private FindCustomerById findCustomerById;

    @Mock
    private FindAllCustomers findAllCustomers;

    @Mock
    private FindCustomerByCPF findCustomerByCPF;

    @Mock
    private CreateNewCustomer createNewCustomer;

    @Mock
    private UpdateCustomer updateCustomer;

    @Mock
    private DeleteCustomerById deleteCustomerById;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void getCustomerById_ShouldReturnCustomer() {
        UUID id = UUID.randomUUID();
        CustomerDomain domain = new CustomerDomain();
        CustomerDTO expectedDTO = new CustomerDTO();

        when(findCustomerById.execute(id)).thenReturn(domain);
        when(modelMapper.map(domain, CustomerDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<CustomerDTO> response = customerController.getCustomerById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        Mockito.verify(findCustomerById).execute(id);
        Mockito.verify(modelMapper).map(domain, CustomerDTO.class);
    }

    @Test
    void getCustomerById_ShouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(findCustomerById.execute(id)).thenThrow(new EntityNotFoundException("Customer not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerController.getCustomerById(id);
        });

        assertEquals("Customer not found", exception.getMessage());
        Mockito.verify(findCustomerById).execute(id);
    }

    @Test
    void getAllCustomers_ShouldReturnCustomers() {
        List<CustomerDomain> domains = List.of(new CustomerDomain(), new CustomerDomain());
        List<CustomerDTO> expectedDTOs = List.of(new CustomerDTO(), new CustomerDTO());

        when(findAllCustomers.execute()).thenReturn(domains);
        when(modelMapper.map(domains.get(0), CustomerDTO.class)).thenReturn(expectedDTOs.get(0));
        when(modelMapper.map(domains.get(1), CustomerDTO.class)).thenReturn(expectedDTOs.get(1));

        ResponseEntity<List<CustomerDTO>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTOs, response.getBody());

        Mockito.verify(findAllCustomers).execute();
        Mockito.verify(modelMapper, Mockito.times(2)).map(Mockito.any(CustomerDomain.class), Mockito.eq(CustomerDTO.class));
    }

    @Test
    void getAllCustomers_ShouldReturnNoContentWhenEmpty() {
        when(findAllCustomers.execute()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CustomerDTO>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        Mockito.verify(findAllCustomers).execute();
    }

    @Test
    void findCustomerByCpf_ShouldReturnCustomer() {
        String cpf = "12345678900";
        CustomerDomain domain = new CustomerDomain();
        CustomerDTO expectedDTO = new CustomerDTO();

        when(findCustomerByCPF.execute(cpf)).thenReturn(domain);
        when(modelMapper.map(domain, CustomerDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<CustomerDTO> response = customerController.findCustomerByCpf(cpf);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        Mockito.verify(findCustomerByCPF).execute(cpf);
        Mockito.verify(modelMapper).map(domain, CustomerDTO.class);
    }

    @Test
    void findCustomerByCpf_ShouldReturnNotFound() {
        String cpf = "12345678900";

        when(findCustomerByCPF.execute(cpf)).thenReturn(null);

        ResponseEntity<CustomerDTO> response = customerController.findCustomerByCpf(cpf);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        Mockito.verify(findCustomerByCPF).execute(cpf);
    }

    @Test
    void saveNewCustomer_ShouldCreateCustomer() {
        CustomerInputDTO inputDTO = new CustomerInputDTO();
        CustomerDomain domain = new CustomerDomain();
        CustomerDomain savedDomain = new CustomerDomain();
        CustomerDTO expectedDTO = new CustomerDTO();

        when(modelMapper.map(inputDTO, CustomerDomain.class)).thenReturn(domain);
        when(createNewCustomer.execute(domain)).thenReturn(savedDomain);
        when(modelMapper.map(savedDomain, CustomerDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<CustomerDTO> response = customerController.saveNewCustomer(inputDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        Mockito.verify(modelMapper).map(inputDTO, CustomerDomain.class);
        Mockito.verify(createNewCustomer).execute(domain);
        Mockito.verify(modelMapper).map(savedDomain, CustomerDTO.class);
    }

    @Test
    void updateCustomer_ShouldUpdateCustomer() {
        UUID id = UUID.randomUUID();
        CustomerInputDTO inputDTO = new CustomerInputDTO();
        CustomerDomain domain = new CustomerDomain();
        domain.setId(id);
        CustomerDomain updatedDomain = new CustomerDomain();
        CustomerDTO expectedDTO = new CustomerDTO();

        when(modelMapper.map(inputDTO, CustomerDomain.class)).thenReturn(domain);
        when(updateCustomer.execute(domain)).thenReturn(updatedDomain);
        when(modelMapper.map(updatedDomain, CustomerDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<CustomerDTO> response = customerController.updateCustomer(id, inputDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        Mockito.verify(modelMapper).map(inputDTO, CustomerDomain.class);
        Mockito.verify(updateCustomer).execute(domain);
        Mockito.verify(modelMapper).map(updatedDomain, CustomerDTO.class);
    }

    @Test
    void deleteCustomerById_ShouldDeleteCustomer() {
        UUID id = UUID.randomUUID();

        Mockito.doNothing().when(deleteCustomerById).execute(id);

        ResponseEntity<Void> response = customerController.deleteCustomerById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Mockito.verify(deleteCustomerById).execute(id);
    }
}
