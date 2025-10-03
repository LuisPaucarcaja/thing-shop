package com.product_service.feature.media.service;

import com.product_service.feature.media.entity.CoverImage;

import java.util.List;

public interface ICoverImageService {

    List<CoverImage> getByCoverGroup(int coverGroup);
}
