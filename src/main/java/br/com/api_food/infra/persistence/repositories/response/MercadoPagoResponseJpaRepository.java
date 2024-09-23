package br.com.api_food.infra.persistence.repositories.response;

import br.com.api_food.infra.persistence.documents.response.MercadoPagoResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoPagoResponseJpaRepository extends MongoRepository<MercadoPagoResponse, String> {
}
