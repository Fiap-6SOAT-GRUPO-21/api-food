package br.com.api_food.infra.persistence.documents.response;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "mercado_pago_responses")
public class MercadoPagoResponse {

    @Id
    private String id;
    private String requestUrl;
    private String requestBody;
    private String responseBody;
    private int statusCode;
    private long timestamp;

}
