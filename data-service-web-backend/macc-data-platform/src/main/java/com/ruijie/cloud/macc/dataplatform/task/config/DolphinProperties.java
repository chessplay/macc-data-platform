package com.ruijie.cloud.macc.dataplatform.task.config;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2023/12/11 10:19
 **/
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dolphin")
@Data
public class DolphinProperties {

    private String address;
    private String token;
    private String tenantCode;
    private Long projectCode;
    private String scriptRootPath;

}
