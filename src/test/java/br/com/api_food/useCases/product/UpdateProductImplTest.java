package br.com.api_food.useCases.product;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.useCases.product.FindProductById;
import br.com.api_food.useCases.product.exceptions.ProductNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductImplTest {

    @Mock
    private FindProductById findProductById;

    @Mock
    private CreateNewProductImpl createNewProduct;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UpdateProductImpl updateProduct;

    @Test
    void should_UpdateCustomerSuccessfully() {
        // Arrange
        var idProduct = UUID.randomUUID();
        var productDomain = new ProductDomain(
                idProduct,
                "Product Name",
                BigDecimal.valueOf(10.00),
                UUID.randomUUID(),
                new StoreDomain(),
                UUID.randomUUID(),
                new CategoryDomain());

        var updatedProduct = new ProductDomain(
                idProduct,
                "Updated Product",
                BigDecimal.valueOf(20.00),
                UUID.randomUUID(),
                new StoreDomain(),
                UUID.randomUUID(),
                new CategoryDomain());

        when(findProductById.execute(idProduct)).thenReturn(productDomain);

        doAnswer(invocation -> {
            ProductDomain source = invocation.getArgument(0);
            ProductDomain target = invocation.getArgument(1);
            target.setName(source.getName());
            target.setPrice(source.getPrice());
            return null;
        }).when(modelMapper).map(updatedProduct, productDomain);

        when(createNewProduct.execute(productDomain)).thenReturn(updatedProduct);

        // Act
        ProductDomain response = updateProduct.execute(updatedProduct);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(idProduct);
        assertThat(response.getName()).isEqualTo("Updated Product");
        assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(20.00));

        verify(findProductById).execute(idProduct);
        verify(modelMapper).map(updatedProduct, productDomain);
        verify(createNewProduct).execute(productDomain);
    }

    @Test
    void should_ThrowException_WhenProductNotFound() {
        // Arrange
        var idProduct = UUID.randomUUID();
        var updatedProduct = new ProductDomain(
                idProduct,
                "Updated Product",
                BigDecimal.valueOf(20.00),
                UUID.randomUUID(),
                new StoreDomain(),
                UUID.randomUUID(),
                new CategoryDomain());

        when(findProductById.execute(idProduct)).thenThrow(ProductNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> updateProduct.execute(updatedProduct))
                .isInstanceOf(ProductNotFound.class);

        verify(findProductById).execute(idProduct);
        verifyNoInteractions(modelMapper);
        verifyNoInteractions(createNewProduct);
    }

}