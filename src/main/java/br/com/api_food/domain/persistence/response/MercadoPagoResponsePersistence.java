package br.com.api_food.domain.persistence.response;

import br.com.api_food.domain.document.response.MercadoPagoResponseDomain;

public interface MercadoPagoResponsePersistence {

    void save(MercadoPagoResponseDomain mercadoPagoResponseDomain);

}
