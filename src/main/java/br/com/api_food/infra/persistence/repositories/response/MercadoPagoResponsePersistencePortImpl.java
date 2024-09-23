package br.com.api_food.infra.persistence.repositories.response;


import br.com.api_food.domain.document.response.MercadoPagoResponseDomain;
import br.com.api_food.domain.persistence.response.MercadoPagoResponsePersistence;
import br.com.api_food.infra.persistence.documents.response.MercadoPagoResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MercadoPagoResponsePersistencePortImpl implements MercadoPagoResponsePersistence {

    private final MercadoPagoResponseJpaRepository mercadoPagoResponseJpaRepository;
    private final ModelMapper modelMapper;

    @Override
    public void save(MercadoPagoResponseDomain mercadoPagoResponseDomain) {
        MercadoPagoResponse mercadoPagoResponse = modelMapper.map(mercadoPagoResponseDomain, MercadoPagoResponse.class);
        mercadoPagoResponseJpaRepository.save(mercadoPagoResponse);
    }

}
