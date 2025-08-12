package ProductMicroservice.feature.product.service.recommendation;

import ProductMicroservice.feature.product.entity.Category;
import ProductMicroservice.feature.product.entity.Product;
import ProductMicroservice.feature.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ProductMicroservice.common.constants.GeneralConstants.DEFAULT_PRODUCT_LIMIT;

@RequiredArgsConstructor
@Service
public class ProductRecommendationService {
    private final ProductRepository productRepository;


    public List<Product> recommendProducts(List<Long> ids) {
        List<Product> products = productRepository.findByIdsWithCategories(ids);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<Product> orderedProducts = ids.stream()
                .map(productMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        int missing = DEFAULT_PRODUCT_LIMIT - orderedProducts.size();

        if (missing > 0 && !orderedProducts.isEmpty()) {
            Long categoryId = getMostSpecificCategoryId(orderedProducts.get(0));

            Page<Product> similarProductsPage = productRepository.findByCategory(
                    categoryId,
                    PageRequest.of(0, missing)
            );

            List<Product> similarProductsPageContent = similarProductsPage.getContent();

            List<Product> similarProductsWithCollections =
                    productRepository.fetchCollectionsForProducts(similarProductsPageContent);

            Set<Long> existingProductIds = orderedProducts.stream()
                    .map(Product::getId)
                    .collect(Collectors.toSet());

            List<Product> filtered = similarProductsWithCollections.stream()
                    .filter(p -> !existingProductIds.contains(p.getId()))
                    .toList();

            orderedProducts.addAll(filtered);
        }

        return orderedProducts;
    }


    private Long getMostSpecificCategoryId(Product product) {
        return product.getCategories().stream()
                .max(Comparator.comparingInt(this::getCategoryDepth))
                .map(Category::getId)
                .orElse(null);
    }

    private int getCategoryDepth(Category category) {
        int depth = 0;
        while (category.getParentCategory() != null) {
            category = category.getParentCategory();
            depth++;
        }
        return depth;
    }
}
