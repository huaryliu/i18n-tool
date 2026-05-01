package com.igix.i18n.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "i18n.extensions")
public class ExtractorConfig {

    private boolean enabled = true;
    private String directory = "extensions";
    private List<String> classes = new ArrayList<>();
}
