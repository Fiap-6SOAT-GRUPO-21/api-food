package br.com.api_food.domain.useCases.customer;

import br.com.api_food.domain.entity.customer.CustomerDomain;

public interface CreateNewCustomer {

    CustomerDomain execute(CustomerDomain customerDomain);
}
