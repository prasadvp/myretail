package com.myretail.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;



@SpringBootApplication(scanBasePackages= {"com.myretail.catalog"})
@EnableMongoRepositories(basePackages = "com.myretail.catalog.persistence.repoistory")
@EnableAsync
@EnableCaching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
