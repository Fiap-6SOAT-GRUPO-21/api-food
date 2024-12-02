package br.com.api_food.useCases.category;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.entity.product.ProductDomain;
import br.com.api_food.domain.persistence.category.CategoryPersistence;
import br.com.api_food.domain.useCases.category.DeleteCategoryById;
import br.com.api_food.domain.useCases.category.FindCategoryById;
import br.com.api_food.domain.useCases.product.FindAllByCategoryId;
import br.com.api_food.useCases.category.exceptions.CategoryNotFound;
import br.com.api_food.useCases.category.exceptions.ExistProductInCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeleteCategoryByIdImpl implements DeleteCategoryById {

    private final CategoryPersistence categoryPersistence;
    private final FindCategoryById findCategoryById;
    private final FindAllByCategoryId findAllByCategoryId;

    @Override
    public void execute(UUID id) {

        CategoryDomain categoryDomain = findCategoryById.execute(id);
        List<ProductDomain> allByCategory = findAllByCategoryId.execute(categoryDomain.getId());

        if (!allByCategory.isEmpty()) {
            String productDetails = allByCategory.stream()
                    .map(product -> "id: " + product.getId() + ", name: " + product.getName())
                    .collect(Collectors.joining(", "));
            throw new ExistProductInCategory("Exist product in category, details: " + productDetails);
        }

        categoryPersistence.deleteByID(id);

    }
}
