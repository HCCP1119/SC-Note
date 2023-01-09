package com.note.file.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Todo
 *
 * @date 2022/12/28 16:45
 **/
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinioProperties {
    @Value("{minio.endpoint}")
    private String endpoint;
    @Value("{minio.accessKey}")
    private String accessKey;
    @Value("{minio.secretKey}")
    private String secretKey;
    @Value("{minio.bucket}")
    private String bucket;
}
