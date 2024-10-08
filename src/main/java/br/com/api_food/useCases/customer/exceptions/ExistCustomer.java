package br.com.api_food.useCases.customer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Customer already exists")
public class ExistCustomer extends RuntimeException {

    public ExistCustomer() {
        super("Customer already exists");
    }
}
