package com.product_service.feature.product.service.recommendation;

import com.product_service.feature.product.entity.Category;
import com.product_service.feature.product.entity.Product;
import com.product_service.feature.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.product_service.common.constants.GeneralConstants.DEFAULT_PRODUCT_LIMIT;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductRecommendationService {

    private final ProductRepository productRepository;

    public List<Product> recommendProducts(List<Long> ids) {
        Optional<Product> mostRecentProduct = productRepository.findByProductId(ids.get(0));
        List<Product> products = productRepository.findByIdsWithPrimaryMedia(ids.stream().skip(1).toList());

        mostRecentProduct.ifPresent(products::add);

        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        log.debug("Mapped products by ID: {}", productMap.keySet());

        List<Product> orderedProducts = ids.stream()
                .map(productMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        log.info("Ordered products after filtering nulls: {}", orderedProducts.size());

        int missing = DEFAULT_PRODUCT_LIMIT - orderedProducts.size();
        log.debug("Missing products to reach limit {}: {}", DEFAULT_PRODUCT_LIMIT, missing);

        if (missing > 0 && !orderedProducts.isEmpty() && mostRecentProduct.isPresent()) {

            Long categoryId = getMostSpecificCategoryId(mostRecentProduct.get());
            log.debug("Most specific category ID found: {}", categoryId);

            if (categoryId == null) {
                return orderedProducts;
            }

            Page<Long> similarProductIds = productRepository.findIdsByCategory(
                    categoryId, PageRequest.of(0, missing));

            List<Product> similarProducts = productRepository.findByIdsWithPrimaryMedia(similarProductIds.getContent());


            Set<Long> existingProductIds = orderedProducts.stream()
                    .map(Product::getId)
                    .collect(Collectors.toSet());

            List<Product> filtered = similarProducts.stream()
                    .filter(p -> !existingProductIds.contains(p.getId()))
                    .toList();

            orderedProducts.addAll(filtered);
        }

        return orderedProducts;
    }

    private Long getMostSpecificCategoryId(Product product) {
        if (product == null) {
            log.error("Product is null in getMostSpecificCategoryId()");
            return null;
        }
        if (product.getCategories() == null || product.getCategories().isEmpty()) {
            log.warn("Product {} has no categories.", product.getId());
            return null;
        }

        log.debug("Finding most specific category for product {}", product.getId());

        Long categoryId = product.getCategories().stream()
                .max(Comparator.comparingInt(this::getCategoryDepth))
                .map(Category::getId)
                .orElse(null);

        log.debug("Most specific category ID for product {}: {}", product.getId(), categoryId);
        return categoryId;
    }

    private int getCategoryDepth(Category category) {
        if (category == null) {
            log.warn("Null category received in getCategoryDepth()");
            return 0;
        }

        int depth = 0;
        while (category.getParentCategory() != null) {
            category = category.getParentCategory();
            depth++;
        }

        log.trace("Category depth calculated: {}", depth);
        return depth;
    }
}
