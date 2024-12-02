package br.com.api_food.useCases.category;

import br.com.api_food.domain.entity.category.CategoryDomain;
import br.com.api_food.domain.persistence.category.CategoryPersistence;
import br.com.api_food.domain.useCases.category.CreateNewCategory;
import br.com.api_food.domain.useCases.category.FindCategoryById;
import br.com.api_food.domain.useCases.category.UpdateCategory;
import br.com.api_food.useCases.category.exceptions.CategoryNotFound;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCategoryImpl implements UpdateCategory {

    private final FindCategoryById findCategoryById;
    private final ModelMapper modelMapper;
    private final CreateNewCategory createNewCategory;

    @Override
    public CategoryDomain execute(CategoryDomain updateCategoryDomain) {
        CategoryDomain domain = findCategoryById.execute(updateCategoryDomain.getId());

        modelMapper.map(updateCategoryDomain, domain);

        return createNewCategory.execute(domain);
    }
}
