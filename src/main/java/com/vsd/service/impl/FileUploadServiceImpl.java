package com.vsd.service.impl;

import com.vsd.entity.ArticleImage;
import com.vsd.service.FileUploadService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final MinioClient minioClient;
    @Value("${spring.minio.bucket-name}")
    private String bucket;

    @Override
    public String uploadArticleImage(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        validate(file);
        String uuid= UUID.randomUUID().toString();
        String extension=file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));

        String objectKey="article"+ File.separator+uuid+extension;

        //uploading code
        if(!minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucket).build())){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());

        }

        minioClient.putObject(PutObjectArgs.builder()

                        .bucket(bucket)
                        .object(objectKey)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType()).build());

        return objectKey;
    }

    @Override
    public String getPreSignUrl(String objectKey) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return
                minioClient.getPresignedObjectUrl(
                       GetPresignedObjectUrlArgs.builder()
                               .bucket(bucket)
                               .object(objectKey)
                               .method(Method.GET)
                               .expiry(60, TimeUnit.MINUTES)
                               .build()
                );
    }

    private static void validate(MultipartFile file){

    }
}
