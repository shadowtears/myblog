package com.xiaomou.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author MouHongDa
 * @date 2022/5/4 22:09
 */
@ConfigurationProperties(prefix = "alioss")
@Component
@Data
public class OssUtil {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String folderName;
}
