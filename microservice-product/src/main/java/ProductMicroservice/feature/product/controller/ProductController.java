package ProductMicroservice.feature.product.controller;

import ProductMicroservice.feature.product.dto.ProductDetailDto;
import ProductMicroservice.feature.product.dto.ProductPreviewDto;
import ProductMicroservice.common.dto.ApiResponse;
import ProductMicroservice.common.dto.PaginatedResponse;
import ProductMicroservice.feature.product.service.IProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static ProductMicroservice.common.constants.GeneralConstants.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        ProductDetailDto productDetail = productService.getProductById(id);
        return new ResponseEntity<>(ApiResponse.success(productDetail), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse> getByCategoryAndBrandAndPrice(@PathVariable Long categoryId,
                                                                     @RequestParam(required = false) Long brand,
                                                                     @RequestParam(required = false) BigDecimal minPrice,
                                                                     @RequestParam(required = false) BigDecimal maxPrice,
                                                             @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page){
        PaginatedResponse<ProductPreviewDto> products = productService.getByCategoryAndBrandAndPriceRange(categoryId,
                brand, minPrice, maxPrice, page);
        return new ResponseEntity<>(ApiResponse.success(products), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}/price-range")
    public ResponseEntity<ApiResponse> getPriceRangeByCategoryAndBrand(@PathVariable Long categoryId,
                                                                          @RequestParam(required = false) Long brand) {
        return new ResponseEntity<>(ApiResponse.success(productService.getPriceRangeByCategoryAndBrand(categoryId,brand)),
                HttpStatus.OK);
    }


    @GetMapping("/random/categories/{categoryId}")
    public ResponseEntity<ApiResponse> getRandomProductsByCategory(@PathVariable Long categoryId) {
        List<ProductPreviewDto> products = productService.getRandomProductsByCategory(categoryId);
        return new ResponseEntity<>(ApiResponse.success(products) , HttpStatus.OK);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<ApiResponse> getProductSuggestions(@Valid @RequestParam @Size(min = MIN_SEARCH_TERM_LENGTH)
                                                             String term) {

        return new ResponseEntity<>(ApiResponse.success(productService.getProductSuggestions(term)), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getProductSearchResults(@Valid @RequestParam @Size(min = MIN_SEARCH_TERM_LENGTH)
                                                           String term,
                                                       @RequestParam(required = false) BigDecimal minPrice,
                                                       @RequestParam(required = false) BigDecimal maxPrice,
                                                       @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page) {
        PaginatedResponse<ProductPreviewDto> products = productService.getProductSearchResults(term, minPrice,
                                                                                            maxPrice, page);
        return new ResponseEntity<>(ApiResponse.success(products), HttpStatus.OK);
    }



    @GetMapping("/search/{searchTerm}/price-range")
    public ResponseEntity<ApiResponse> getPriceRangeBySearchTerm(@Valid @PathVariable @Size(min= MIN_SEARCH_TERM_LENGTH)
                                                                      String searchTerm){
        return ResponseEntity.ok(ApiResponse.success(productService.getPriceRangeBySearchTerm(searchTerm)));
    }


    @GetMapping
    public ResponseEntity<ApiResponse> getByIds(@RequestParam List<Long> ids){
        return new ResponseEntity<>(ApiResponse.success(productService.getProductsByIds(ids)), HttpStatus.OK);
    }

    @GetMapping("/recommended")
    public ResponseEntity<ApiResponse> getRecommendedProducts(@RequestParam List<Long> ids){
        return new ResponseEntity<>(ApiResponse.success(productService.recommendProducts(ids)), HttpStatus.OK);
    }






}

