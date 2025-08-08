package com.microservice_auth.infra.storage;

import com.microservice_auth.common.exception.FileUploadException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static}") String region,
            @Value("${cloud.aws.s3.bucket}") String bucketName  )
    {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(region))
                .build();
        this.bucketName = bucketName;
    }


    public String uploadFile(MultipartFile file, String destinationPath)  {
        try {
            String fileName = destinationPath + "/" + generateFileName(file);

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);

        } catch (FileUploadException e) {
            throw new FileUploadException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public String uploadImageFromUrl(String imageUrl, String destinationPath) {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            String contentType = new Tika().detect(inputStream);

            String fileName = destinationPath + "/" + UUID.randomUUID() + ".jpg";

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(contentType)
                            .build(),
                    RequestBody.fromInputStream(inputStream, inputStream.available())
            );

            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);

        } catch (IOException e) {
            throw new FileUploadException("Error uploading image from URL: " + e.getMessage());
        }
    }


    public void deletePreviousImageFromS3(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains(bucketName)) {
            return;
        }

        try {
            URL url = new URL(imageUrl);
            String fileKey = url.getPath().substring(1);

            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete S3 object: " + e.getMessage());
        }
    }


    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll(" ", "_");
    }

}