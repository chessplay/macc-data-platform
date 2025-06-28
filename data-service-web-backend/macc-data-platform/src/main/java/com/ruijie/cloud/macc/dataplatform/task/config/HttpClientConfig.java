package com.ruijie.cloud.macc.dataplatform.task.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory; // 可选，用于配置超时
import org.springframework.web.client.RestTemplate;
// import java.util.ArrayList; // 可选，用于拦截器
// import java.util.List; // 可选，用于拦截器
// import org.springframework.http.client.ClientHttpRequestInterceptor; // 可选，用于拦截器

@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 可选：如果您需要更精细的控制，例如设置连接和读取超时
        // 您需要添加 Apache HttpClient 依赖：
        // <dependency>
        //     <groupId>org.apache.httpcomponents</groupId>
        //     <artifactId>httpclient</artifactId>
        //     <version>4.5.13</version> // </dependency>
        /*
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000); // 连接目标服务器超时时间，单位毫秒
        requestFactory.setReadTimeout(10000);    // 从目标服务器读取响应超时时间，单位毫秒
        // requestFactory.setConnectionRequestTimeout(2000); // 从连接池获取连接的超时时间，单位毫秒

        // 如果需要更复杂的HttpClient配置 (例如连接池, SSL, 代理等)
        // CloseableHttpClient httpClient = HttpClientBuilder.create()
        // .setConnectionManager(poolingConnectionManager()) // 配置连接池
        // .setDefaultRequestConfig(RequestConfig.custom()...) // 默认请求配置
        // .build();
        // requestFactory.setHttpClient(httpClient);

        restTemplate.setRequestFactory(requestFactory);
        */

        // 可选：添加自定义拦截器 (例如，用于日志记录、统一添加Header等)
        /*
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        // 假设您有一个自定义的 LoggingInterceptor实现了ClientHttpRequestInterceptor接口
        // interceptors.add(new LoggingInterceptor());
        restTemplate.setInterceptors(interceptors);
        */

        return restTemplate;
    }

    // 如果配置了连接池，可以像这样定义一个PoolingHttpClientConnectionManager Bean
    /*
    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(200); // 连接池最大连接数
        poolingConnectionManager.setDefaultMaxPerRoute(20); // 每个路由（域名）的最大连接数
        return poolingConnectionManager;
    }
    */
}