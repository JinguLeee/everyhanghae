package com.example.everyhanghae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EveryhanghaeApplication {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
    public static void main(String[] args) {
        SpringApplication.run(EveryhanghaeApplication.class, args);
    }

}
