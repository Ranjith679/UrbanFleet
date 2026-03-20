package com.urbanfleet.restaurant_service.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

//        if (!file.getContentType().startsWith("image/")) {
//            throw new RuntimeException("Only image files are allowed");
//        }
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return "http://localhost:9000/" + bucketName + "/" + fileName;
    }

    public List<String> uploadFiles(List<MultipartFile> files) throws Exception {

        if (files == null || files.isEmpty()) {
            throw new RuntimeException("No files provided");
        }

        if (files.size() > 5) {
            throw new RuntimeException("Maximum 5 images allowed");
        }

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            String url = uploadFile(file);  // reuse single upload
            urls.add(url);
        }

        return urls;
    }
}
