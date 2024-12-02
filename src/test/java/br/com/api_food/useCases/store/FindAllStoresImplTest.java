package br.com.api_food.useCases.store;

import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.entity.store.payment.MercadoPagoGatewayDomain;
import br.com.api_food.domain.persistence.store.StorePersistence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllStoresImplTest {

    @Mock
    private  StorePersistence storePersistence;

    @InjectMocks
    private FindAllStoresImpl findAllStores;

    @Test
    void should_ReturnAllStores() {

        // Arrange
        var mercadoPagoDomain = new MercadoPagoGatewayDomain("Collectors", "externalPos");
        var storeDomain1 = new StoreDomain(UUID.randomUUID(), "Store Name 1", true, UUID.randomUUID(), mercadoPagoDomain);
        var storeDomain2 = new StoreDomain(UUID.randomUUID(), "Store Name 2", true, UUID.randomUUID(), mercadoPagoDomain);

        when(storePersistence.findAll()).thenReturn(List.of(storeDomain1, storeDomain2));

        // Act
        List<StoreDomain> response = findAllStores.execute();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response).containsExactlyInAnyOrder(storeDomain1, storeDomain2);
        verify(storePersistence).findAll();
    }
}