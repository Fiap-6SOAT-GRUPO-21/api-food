package br.com.api_food.useCases.product;

import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.persistence.product.ProductPersistence;
import br.com.api_food.useCases.product.exceptions.ProductNotFound;
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
class FindProductByIdImplTest {

    @Mock
    private ProductPersistence productPersistence;

    @InjectMocks
    private FindProductByIdImpl findProductById;

    @Test
    void should_ReturnProduct_WhenIdExists() {
        // Arrange
        var id = UUID.randomUUID();
        var product = new ProductDomain();
        product.setId(id);

        when(productPersistence.findById(id)).thenReturn(Optional.of(product));

        // Act
        ProductDomain result = findProductById.execute(id);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(productPersistence).findById(id);
    }

    @Test
    void should_ThrowException_WhenIdDoesNotExist() {
        // Arrange
        var id = UUID.randomUUID();
        when(productPersistence.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> findProductById.execute(id))
                .isInstanceOf(ProductNotFound.class);

        verify(productPersistence).findById(id);
    }

}