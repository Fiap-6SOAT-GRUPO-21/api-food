package br.com.api_food.application.controller.product;

import br.com.api_food.application.dtos.product.ProductDTO;
import br.com.api_food.application.dtos.product.ProductInputDTO;
import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.useCases.product.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private FindProductById findProductById;

    @Mock
    private FindAllByCategoryId findAllByCategoryId;

    @Mock
    private FindAllProducts findAllProducts;

    @Mock
    private CreateNewProduct createNewProduct;

    @Mock
    private UpdateProduct updateProduct;

    @Mock
    private DeleteProductById deleteProductById;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void getById_ShouldReturnProduct() {
        UUID id = UUID.randomUUID();
        ProductDomain domain = new ProductDomain();
        ProductDTO expectedDTO = new ProductDTO();

        when(findProductById.execute(id)).thenReturn(domain);
        when(modelMapper.map(domain, ProductDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<ProductDTO> response = productController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        Mockito.verify(findProductById).execute(id);
        Mockito.verify(modelMapper).map(domain, ProductDTO.class);
    }

    @Test
    void getById_ShouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(findProductById.execute(id)).thenThrow(new EntityNotFoundException("Product not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productController.getById(id));

        assertEquals("Product not found", exception.getMessage());
        Mockito.verify(findProductById).execute(id);
    }

    @Test
    void getByIdCategory_ShouldReturnProducts() {
        UUID categoryId = UUID.randomUUID();
        List<ProductDomain> domains = List.of(new ProductDomain(), new ProductDomain());
        List<ProductDTO> expectedDTOs = List.of(new ProductDTO(), new ProductDTO());

        when(findAllByCategoryId.execute(categoryId)).thenReturn(domains);
        when(modelMapper.map(domains.get(0), ProductDTO.class)).thenReturn(expectedDTOs.get(0));
        when(modelMapper.map(domains.get(1), ProductDTO.class)).thenReturn(expectedDTOs.get(1));

        ResponseEntity<List<ProductDTO>> response = productController.getByIdCategory(categoryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTOs, response.getBody());

        verify(findAllByCategoryId).execute(categoryId);
        verify(modelMapper, Mockito.times(2)).map(Mockito.any(ProductDomain.class), Mockito.eq(ProductDTO.class));
    }

    @Test
    void getByIdCategory_ShouldReturnNoContentWhenEmpty() {
        UUID categoryId = UUID.randomUUID();

        when(findAllByCategoryId.execute(categoryId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProductDTO>> response = productController.getByIdCategory(categoryId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        Mockito.verify(findAllByCategoryId).execute(categoryId);
    }

    @Test
    void getAll_ShouldReturnAllProducts() {
        List<ProductDomain> domains = List.of(new ProductDomain(), new ProductDomain());
        List<ProductDTO> expectedDTOs = List.of(new ProductDTO(), new ProductDTO());

        when(findAllProducts.execute()).thenReturn(domains);
        when(modelMapper.map(domains.get(0), ProductDTO.class)).thenReturn(expectedDTOs.get(0));
        when(modelMapper.map(domains.get(1), ProductDTO.class)).thenReturn(expectedDTOs.get(1));

        ResponseEntity<List<ProductDTO>> response = productController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTOs, response.getBody());

        Mockito.verify(findAllProducts).execute();
        Mockito.verify(modelMapper, Mockito.times(2)).map(Mockito.any(ProductDomain.class), Mockito.eq(ProductDTO.class));
    }

    @Test
    void save_ShouldCreateNewProduct() {
        ProductInputDTO inputDTO = new ProductInputDTO();
        ProductDomain domain = new ProductDomain();
        ProductDomain savedDomain = new ProductDomain();
        ProductDTO expectedDTO = new ProductDTO();

        when(modelMapper.map(inputDTO, ProductDomain.class)).thenReturn(domain);
        when(createNewProduct.execute(domain)).thenReturn(savedDomain);
        when(modelMapper.map(savedDomain, ProductDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<ProductDTO> response = productController.save(inputDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        verify(modelMapper).map(inputDTO, ProductDomain.class);
        verify(createNewProduct).execute(domain);
        verify(modelMapper).map(savedDomain, ProductDTO.class);
    }

    @Test
    void update_ShouldReturnUpdatedProduct() {
        UUID id = UUID.randomUUID();
        ProductInputDTO inputDTO = new ProductInputDTO();
        ProductDomain domain = new ProductDomain();
        domain.setId(id);
        ProductDomain updatedDomain = new ProductDomain();
        ProductDTO expectedDTO = new ProductDTO();

        when(modelMapper.map(inputDTO, ProductDomain.class)).thenReturn(domain);
        when(updateProduct.execute(domain)).thenReturn(updatedDomain);
        when(modelMapper.map(updatedDomain, ProductDTO.class)).thenReturn(expectedDTO);

        ResponseEntity<ProductDTO> response = productController.update(id, inputDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());

        verify(modelMapper).map(inputDTO, ProductDomain.class);
        verify(updateProduct).execute(domain);
        verify(modelMapper).map(updatedDomain, ProductDTO.class);
    }

    @Test
    void delete_ShouldDeleteProduct() {
        UUID id = UUID.randomUUID();

        doNothing().when(deleteProductById).execute(id);

        ResponseEntity<Void> response = productController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(deleteProductById).execute(id);
    }
}