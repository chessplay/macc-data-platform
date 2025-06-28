package com.ruijie.cloud.macc.dataplatform.task.config;



import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "platform.task.yarn-app-id.compensation-job")
@Data
public class YarnAppIdCompensationProperties {
    private boolean enabled = false; // 默认关闭
    private String cron = "0 0/10 * * * ?"; // 默认每10分钟
    private int initialDelayMinutes = 5;
    private int maxAgeHoursForCheck = 24;
    private int logFetchAttempts = 2;
    private long logFetchDelayMs = 10000;
}
