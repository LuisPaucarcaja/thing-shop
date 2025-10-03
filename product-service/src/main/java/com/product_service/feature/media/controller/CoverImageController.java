package com.product_service.feature.media.controller;

import com.product_service.common.dto.ApiResponse;
import com.product_service.feature.media.entity.CoverImage;
import com.product_service.feature.media.service.ICoverImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cover-images")
public class CoverImageController {

    private final ICoverImageService coverImageService;

    @GetMapping("/groups/{id}")
    public ResponseEntity<ApiResponse> getCoverImagesByCoverGroup(@PathVariable int id){
        List<CoverImage> coverImages = coverImageService.getByCoverGroup(id);
        return new ResponseEntity<>(ApiResponse.success(coverImages), HttpStatus.OK);
    }
}
