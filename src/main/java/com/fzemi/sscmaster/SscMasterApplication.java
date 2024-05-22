package com.fzemi.sscmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SscMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SscMasterApplication.class, args);
    }

}
