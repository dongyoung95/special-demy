package com.specialdemy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpecialDemyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpecialDemyApplication.class, args);
    }
}
