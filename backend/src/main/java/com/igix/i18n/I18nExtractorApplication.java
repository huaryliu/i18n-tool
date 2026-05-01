package com.abc.i18n;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.abc.i18n")
public class I18nExtractorApplication {

    public static void main(String[] args) {
        SpringApplication.run(I18nExtractorApplication.class, args);
    }
}
