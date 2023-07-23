package com.executor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.generation.data-types")
public class DataTypeConfig {
    private String stringType;
    private String integerType;
    private String doubleType;
    private String datetimeType;
    private String[] supportedDataTypes;
}
