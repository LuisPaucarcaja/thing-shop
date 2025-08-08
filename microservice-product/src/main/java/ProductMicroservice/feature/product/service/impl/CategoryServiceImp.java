package ProductMicroservice.feature.product.service.impl;

import ProductMicroservice.feature.product.dto.BrandDto;
import ProductMicroservice.feature.product.dto.CategoryDto;
import ProductMicroservice.feature.product.mapper.BrandMapper;
import ProductMicroservice.feature.product.mapper.CategoryMapper;
import ProductMicroservice.feature.product.repository.CategoryRepository;
import ProductMicroservice.feature.product.repository.ProductRepository;
import ProductMicroservice.feature.product.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ProductMicroservice.common.constants.CacheConstants.GET_ALL_CATEGORIES;
import static ProductMicroservice.common.constants.CacheConstants.GET_BRANDS_BY_CATEGORY;

@RequiredArgsConstructor
@Service
public class CategoryServiceImp implements ICategoryService {


    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final BrandMapper brandMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Cacheable(GET_ALL_CATEGORIES)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Cacheable(GET_BRANDS_BY_CATEGORY)
    public List<BrandDto> getBrandsByCategory(Long categoryId) {
        return productRepository.findBrandsByCategoryId(categoryId).stream()
                .map(brandMapper::toBrandDto).collect(Collectors.toList());
    }

}
