package com.tiertagger.api;

import com.tiertagger.api.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TierTagger API - Ponto de entrada da aplicação Spring Boot 3.
 */
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableAsync
@EnableScheduling
public class TierTaggerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TierTaggerApiApplication.class, args);
    }
}

