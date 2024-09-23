package br.com.api_food.domain.document.response;

import br.com.api_food.domain.entity.DomainEntity;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MercadoPagoResponseDomain extends DomainEntity {

    private String requestUrl;
    private String requestBody;
    private String responseBody;
    private int statusCode;
    private long timestamp;

}
