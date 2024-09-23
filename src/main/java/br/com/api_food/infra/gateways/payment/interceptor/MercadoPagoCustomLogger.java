package br.com.api_food.infra.gateways.payment.interceptor;

import br.com.api_food.domain.document.response.MercadoPagoResponseDomain;
import br.com.api_food.domain.persistence.response.MercadoPagoResponsePersistence;
import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class MercadoPagoCustomLogger extends Logger {

    @Autowired
    private MercadoPagoResponsePersistence mercadoPagoResponsePersistence;

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        log.info("Request: {}", request.url());
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String responseBody = response.body() != null
                ? new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8)
                : null;

        MercadoPagoResponseDomain log = MercadoPagoResponseDomain.builder()
                .requestUrl(response.request().url())
                .requestBody(response.request().body() != null ? new String(response.request().body(), StandardCharsets.UTF_8) : null)
                .responseBody(responseBody)
                .statusCode(response.status())
                .timestamp(System.currentTimeMillis())
                .build();

        mercadoPagoResponsePersistence.save(log);

        // Retornar a resposta para o Feign continuar o processamento
        return response.toBuilder()
                .body(responseBody.getBytes(StandardCharsets.UTF_8))
                .build();
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info("{} {}", methodTag(configKey) + format, args);
        log.info("{} {}", methodTag(configKey) + format + "%n", args);
    }

}
