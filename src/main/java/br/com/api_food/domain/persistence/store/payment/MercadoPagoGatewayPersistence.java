package br.com.api_food.domain.persistence.store.payment;

import br.com.api_food.domain.entity.store.payment.MercadoPagoGatewayDomain;

import java.util.Optional;
import java.util.UUID;

public interface MercadoPagoGatewayPersistence {

    Optional<MercadoPagoGatewayDomain> findById(UUID id);

    MercadoPagoGatewayDomain save(MercadoPagoGatewayDomain mercadoPagoGatewayDomain);
}
