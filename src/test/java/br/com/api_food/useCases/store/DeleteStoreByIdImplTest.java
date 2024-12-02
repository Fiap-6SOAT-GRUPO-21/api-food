package br.com.api_food.useCases.store;

import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.entity.store.payment.MercadoPagoGatewayDomain;
import br.com.api_food.domain.persistence.store.StorePersistence;
import br.com.api_food.domain.useCases.store.FindStoreById;
import br.com.api_food.useCases.customer.exceptions.CustomerNotFound;
import br.com.api_food.useCases.store.exceptions.StoreNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteStoreByIdImplTest {

    @Mock
    private StorePersistence storePersistence;

    @Mock
    private FindStoreById findStoreById;

    @InjectMocks
    private DeleteStoreByIdImpl deleteStoreById;

    private StoreDomain storeDomain;

    @BeforeEach
    void setUp() {

        var mercadoPagoDomain = new MercadoPagoGatewayDomain("Collectors", "externalPos");
        var storeId = UUID.fromString("4b327e9a-9371-4172-8630-1bb92dbf9dfc");
        storeDomain = new StoreDomain(storeId, "Store Name", true, UUID.randomUUID(), mercadoPagoDomain);
    }

    @Test
    void should_DeleteStoreSuccessfully() {
        // Arrange
        var storeId = UUID.randomUUID();
        when(findStoreById.execute(storeId)).thenReturn(storeDomain);

        // Act
        deleteStoreById.execute(storeId);

        // Assert
        verify(findStoreById).execute(storeId);
        verify(storePersistence).save(storeDomain);
        verifyNoMoreInteractions(findStoreById, storePersistence);
    }

    @Test
    void should_ThrowException_WhenStoreNotFound() {

        // Arrange
        var storeId = UUID.randomUUID();
        when(findStoreById.execute(storeId)).thenThrow(StoreNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> deleteStoreById.execute(storeId))
                .isInstanceOf(StoreNotFound.class);

        verify(findStoreById).execute(storeId);
        verifyNoInteractions(storePersistence);
    }

}