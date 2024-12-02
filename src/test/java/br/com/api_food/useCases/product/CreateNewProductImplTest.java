package br.com.api_food.useCases.product;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.entity.store.StoreDomain;
import br.com.api_food.domain.persistence.product.ProductPersistence;
import br.com.api_food.domain.useCases.category.FindCategoryById;
import br.com.api_food.domain.useCases.store.FindStoreById;
import br.com.api_food.useCases.category.exceptions.CategoryNotFound;
import br.com.api_food.useCases.product.exceptions.ProductInvalidPrice;
import br.com.api_food.useCases.store.exceptions.StoreNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateNewProductImplTest {

    @Mock
    private FindCategoryById findCategoryById;

    @Mock
    private FindStoreById findStoreById;

    @Mock
    private ProductPersistence productPersistence;

    @InjectMocks
    private CreateNewProductImpl createNewProduct;

    private ProductDomain productDomain;

    @BeforeEach
    void setUp() {
        productDomain = new ProductDomain(UUID.randomUUID(),
                "Product Name",
                BigDecimal.valueOf(10.00),
                UUID.randomUUID(),
                new StoreDomain(),
                UUID.randomUUID(),
                new CategoryDomain());
    }

    @Test
    void should_SaveProduct_WhenValidData() {
        // Arrange
        when(productPersistence.save(productDomain)).thenReturn(productDomain);

        // Act
        ProductDomain result = createNewProduct.execute(productDomain);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(productDomain);
        verify(findCategoryById).execute(productDomain.getIdCategory());
        verify(findStoreById).execute(productDomain.getIdStore());
        verify(productPersistence).save(productDomain);
    }

    @Test
    void should_ThrowException_WhenPriceIsZeroOrNegative() {
        // Arrange
        productDomain.setPrice(BigDecimal.ZERO);

        // Act & Assert
        assertThatThrownBy(() -> createNewProduct.execute(productDomain))
                .isInstanceOf(ProductInvalidPrice.class);

        verifyNoInteractions(productPersistence);
    }

    @Test
    void should_ThrowException_WhenPriceIsNegative() {
        // Arrange
        productDomain.setPrice(BigDecimal.valueOf(-1.00));

        // Act & Assert
        assertThatThrownBy(() -> createNewProduct.execute(productDomain))
                .isInstanceOf(ProductInvalidPrice.class);


        verifyNoInteractions(productPersistence);
    }

    @Test
    void should_ThrowException_WhenCategoryIdDoesNotExist() {
        // Arrange
        when(findCategoryById.execute(productDomain.getIdCategory())).thenThrow(CategoryNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> createNewProduct.execute(productDomain))
                .isInstanceOf(CategoryNotFound.class);

        verify(findCategoryById).execute(productDomain.getIdCategory());
        verifyNoInteractions(findStoreById);
        verifyNoInteractions(productPersistence);
    }

    @Test
    void should_ThrowException_WhenStoreIdDoesNotExist() {
        // Arrange
        when(findStoreById.execute(productDomain.getIdStore())).thenThrow(StoreNotFound.class);

        // Act & Assert
        assertThatThrownBy(() -> createNewProduct.execute(productDomain))
                .isInstanceOf(StoreNotFound.class);

        verify(findStoreById).execute(productDomain.getIdStore());
        verifyNoInteractions(productPersistence);
    }
}