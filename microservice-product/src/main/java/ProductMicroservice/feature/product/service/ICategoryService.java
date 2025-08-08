package ProductMicroservice.feature.product.service;

import ProductMicroservice.feature.product.dto.BrandDto;
import ProductMicroservice.feature.product.dto.CategoryDto;

import java.util.List;

public interface ICategoryService {
    List<CategoryDto> getAllCategories();
    List<BrandDto> getBrandsByCategory(Long categoryId);
}
