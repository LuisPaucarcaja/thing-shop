package com.microservice_auth.feature.user.service.helper;

import com.microservice_auth.infra.storage.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import static com.microservice_auth.common.constants.GeneralConstants.MAXIMUM_USER_AVATAR_SIZE;
import static com.microservice_auth.common.util.ImageValidator.validateImageFile;

@Service
@RequiredArgsConstructor
public class UserAvatarService {

    private final S3Service s3Service;

    public String uploadAvatarFromUrl(String externalUrl, Long userId) {
        return s3Service.uploadImageFromUrl(externalUrl, "users/" + userId);
    }
    public String uploadAvatar(MultipartFile file, Long userId) {
        validateImageFile(file, MAXIMUM_USER_AVATAR_SIZE);
        String folder = "users/" + userId;
        return s3Service.uploadFile(file, folder);
    }

    public void deleteAvatar(String avatarUrl) {
        s3Service.deletePreviousImageFromS3(avatarUrl);
    }

    public String handleAvatarChange(MultipartFile newFile, boolean removeAvatar,
                                     String currentAvatarUrl, Long userId) {

        if (removeAvatar) {
            if (currentAvatarUrl != null) {
                deleteAvatar(currentAvatarUrl);
            }
            return null;
        }

        if (newFile != null && !newFile.isEmpty()) {
            String avatar = uploadAvatar(newFile, userId);
            if (currentAvatarUrl != null) {
                deleteAvatar(currentAvatarUrl);
            }

            return avatar;
        }

        return currentAvatarUrl;
    }


}
