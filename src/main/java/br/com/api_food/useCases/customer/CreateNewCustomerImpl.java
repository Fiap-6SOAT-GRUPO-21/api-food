package br.com.api_food.useCases.customer;

import br.com.api_food.domain.entity.customer.CustomerDomain;
import br.com.api_food.domain.persistence.customer.CustomerPersistence;
import br.com.api_food.domain.useCases.customer.CreateNewCustomer;
import br.com.api_food.domain.useCases.store.FindStoreById;
import br.com.api_food.useCases.customer.exceptions.ExistCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNewCustomerImpl implements CreateNewCustomer {

    private final CustomerPersistence customerPersistence;
    private final FindStoreById findStoreById;

    @Override
    public CustomerDomain execute(CustomerDomain customerDomain) {
        customerPersistence.findByCpf(customerDomain.getCpf()).ifPresent(pessoa -> {
            throw new ExistCustomer();
        });

        findStoreById.execute(customerDomain.getIdStore());
        customerDomain.setCpf(customerDomain.getCpf().replaceAll("[^0-9]", ""));
        customerDomain.setName(customerDomain.getName());
        customerDomain.setEmail(customerDomain.getEmail());

        return customerPersistence.save(customerDomain);
    }
}
