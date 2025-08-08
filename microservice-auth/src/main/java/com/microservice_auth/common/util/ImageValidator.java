package com.microservice_auth.common.util;


import com.microservice_auth.common.exception.FileUploadException;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public class ImageValidator {

    private static final Set<String> SUPPORTED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/jpg"
    );

    public static void validateImageFile(MultipartFile file, long maxFileSize) {
        String contentType = file.getContentType();
        if (contentType == null || !SUPPORTED_IMAGE_TYPES.contains(contentType) || !isValidImage(file)) {
            throw new FileUploadException("Only valid image files are allowed");
        }

        if (file.getSize() > maxFileSize) {
            throw new FileUploadException("File size exceeds the limit of " + (maxFileSize / 1024 / 1024) + "MB");
        }

    }

    private static boolean isValidImage(MultipartFile file) {
        try {
            Tika tika = new Tika();
            String detectedType = tika.detect(file.getInputStream());
            return detectedType.startsWith("image/");
        } catch (IOException e) {
            throw new FileUploadException("Invalid image file content");
        }
    }


}
