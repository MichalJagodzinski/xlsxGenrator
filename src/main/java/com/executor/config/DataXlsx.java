package com.executor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.generation.jfsmql")
public class DataXlsx {
    private String stringQuote;
    private String datetimeSeparator;
    private String separator;
    private String functionOpenBracket;
    private String functionCloseBracket;
    private String vectorOpenBracket;
    private String vectorCloseBracket;
}
