package br.com.api_food.useCases.store;

import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.persistence.store.StorePersistence;
import br.com.api_food.useCases.store.exceptions.StoreNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindStoreByIdImplTest {

    @Mock
    private StorePersistence storePersistence;

    @InjectMocks
    private FindStoreByIdImpl findStoreById;

    @Test
    void should_ReturnStore_WhenIdExists() {
        // Arrange
        var storeId = UUID.randomUUID();
        var storeDomain = new StoreDomain();
        storeDomain.setId(storeId);

        when(storePersistence.findById(storeId)).thenReturn(Optional.of(storeDomain));

        // Act
        StoreDomain result = findStoreById.execute(storeId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(storeId);
        verify(storePersistence).findById(storeId);
    }

    @Test
    void should_ThrowException_WhenIdDoesNotExist() {
        // Arrange
        var storeId = UUID.randomUUID();
        when(storePersistence.findById(storeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> findStoreById.execute(storeId))
                .isInstanceOf(StoreNotFound.class);

        verify(storePersistence).findById(storeId);
    }
}