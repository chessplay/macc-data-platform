package com.ruijie.cloud.macc.dataplatform.task.config;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2023/12/11 10:21
 **/

import com.ruijie.cloud.macc.dataplatform.task.ds.core.DolphinClient;
import com.ruijie.cloud.macc.dataplatform.task.ds.remote.DolphinsRestTemplate;
import com.ruijie.cloud.macc.dataplatform.task.ds.remote.request.DefaultHttpClientRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.RequestContent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DolphinClientConfig {

    private final DolphinProperties dolphinProperties;

    public DolphinClientConfig(DolphinProperties dolphinProperties) {
        this.dolphinProperties = dolphinProperties;
    }

    @Bean
    public DolphinsRestTemplate dolphinsRestTemplate() {
        return new DolphinsRestTemplate(
            new DefaultHttpClientRequest(
                HttpClients.custom()
                    .addInterceptorLast(new RequestContent(true))
                    .setDefaultRequestConfig(RequestConfig.custom().build())
                    .build(),
                RequestConfig.custom().build()));
    }

    @Bean
    public DolphinClient dolphinClient(DolphinsRestTemplate restTemplate) {
        return new DolphinClient(dolphinProperties.getToken(), dolphinProperties.getAddress(), restTemplate);
    }
}

