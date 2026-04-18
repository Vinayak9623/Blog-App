package com.vsd.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIoConfig {

    @Value("${spring.minio.endpoint}")
    String url;
    @Value("${spring.minio.access-key}")
    String accessKey;
    @Value("${spring.minio.secret-key}")
    String secretKey;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey,secretKey)
                .build();
    }
}
