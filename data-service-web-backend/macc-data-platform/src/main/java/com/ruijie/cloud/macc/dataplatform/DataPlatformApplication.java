package com.ruijie.cloud.macc.dataplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan({"com.ruijie.cloud"})
@EnableAsync(proxyTargetClass = true)
@EnableTransactionManagement
@MapperScan("com.ruijie.cloud.**.mapper")
@EnableScheduling
public class DataPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataPlatformApplication.class, args);
	}

}

