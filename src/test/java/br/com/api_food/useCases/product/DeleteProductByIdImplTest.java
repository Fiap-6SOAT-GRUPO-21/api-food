package br.com.api_food.useCases.product;

import br.com.api_food.domain.persistence.product.ProductPersistence;
import br.com.api_food.domain.useCases.product.FindProductById;
import br.com.api_food.useCases.product.exceptions.ProductNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductByIdImplTest {

    @Mock
    private FindProductById findProductById;

    @Mock
    private ProductPersistence productPersistence;

    @InjectMocks
    private DeleteProductByIdImpl deleteProductById;

    @Test
    void should_DeleteProductSuccessfully() {
        // Arrange
        var productId = UUID.randomUUID();

        doNothing().when(productPersistence).deleteByID(productId);

        // Act
        deleteProductById.execute(productId);

        // Assert
        verify(findProductById).execute(productId);
        verify(productPersistence).deleteByID(productId);
        verifyNoMoreInteractions(findProductById, productPersistence);
    }

    @Test
    void should_ThrowException_WhenProductNotFound() {

        // Arrange
        var productId = UUID.randomUUID();
        when(findProductById.execute(productId)).thenThrow(ProductNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> deleteProductById.execute(productId))
                .isInstanceOf(ProductNotFound.class);

        verify(findProductById).execute(productId);
        verifyNoInteractions(productPersistence);
    }

}