package com.example.eCommerceApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@ComponentScan(basePackages = {"controller", "repository", "com.example.eCommerceApp"})
@EnableJpaRepositories("repository")
@EntityScan("model")
@EnableAutoConfiguration
public class ECommerceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceAppApplication.class, args);
	}

	@Bean
	public AtomicInteger getAtomicInteger() {
		return new AtomicInteger();
	}
}
