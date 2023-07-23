package com.executor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.generation.functions")
public class DataFunctions {
    private String oneOf;
    private String range;
    private String dict;
    private String[] supportedFunctions;
}
