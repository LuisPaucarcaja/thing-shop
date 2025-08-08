package ProductMicroservice.feature.media.service;

import ProductMicroservice.feature.media.entity.CoverImage;

import java.util.List;

public interface ICoverImageService {

    List<CoverImage> getByCoverGroup(int coverGroup);
}
