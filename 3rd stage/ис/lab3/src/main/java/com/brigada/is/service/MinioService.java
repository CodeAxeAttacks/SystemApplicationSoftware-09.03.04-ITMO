package com.brigada.is.service;

import com.brigada.is.exception.ImportException;
import com.brigada.is.exception.NotFoundException;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;
    private final String bucket = "import-files";

    public InputStreamResource downloadFile(String fileName) {
        if (isObjectExist(fileName)) {
            try {
                InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(fileName)
                                .build()
                );
                return new InputStreamResource(inputStream);
            } catch (Exception e) {
                throw new ImportException("Failed to download file: " + fileName, e);
            }
        } else {
            throw new NotFoundException("File not found: " + fileName);
        }
    }

    public void uploadFileToMinIO(MultipartFile file, String fileName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new ImportException("Failed to upload file to MinIO", e);
        }
    }

    public void deleteFileFromMinIO(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isObjectExist(String name) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(name).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
