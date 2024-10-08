package br.com.api_food.domain.persistence.product;

import br.com.api_food.domain.entity.product.ProductDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPersistence {

    Optional<ProductDomain> findById(UUID id);

    Optional<ProductDomain> findByIdAndIdStore(UUID idProduct, UUID idStore);

    ProductDomain save(ProductDomain productDomain);

    void deleteByID(UUID id);

    List<ProductDomain> findAll();

    List<ProductDomain> findAllByCategory(UUID idCategory);
}
