package com.product_service.feature.product.controller;

import com.product_service.common.exception.ResourceNotFoundException;
import com.product_service.feature.product.dto.ProductDetailDto;
import com.product_service.feature.product.entity.Product;
import com.product_service.feature.product.service.IProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.product_service.common.constants.GeneralConstants.FIELD_ID;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @Test
    void getProductById_WhenProductIsFound() throws Exception {
        // Arrange
        ProductDetailDto product = new ProductDetailDto();
        product.setId(1L);
        product.setName("test");


        when(productService.getProductById(anyLong())).thenReturn(product);
        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("test"))
                .andReturn();

        // Assert
        assertEquals(200, mvcResult.getResponse().getStatus());

    }


    @Test
    void getProductById_WhenProductNotFound() throws Exception {
        // Arrange

        when(productService.getProductById(anyLong())).thenThrow(
                new ResourceNotFoundException(Product.class.getSimpleName(), FIELD_ID, 1L));
        // Act and Ass
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();


    }


    @Test
    void getByCategoryAndBrandAndPrice_WithCategory(){


    }
}