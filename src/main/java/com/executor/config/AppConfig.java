package com.executor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.default.rand")
public class AppConfig {
    private int maxValueInteger;
    private int maxValueDouble;
    private int maxDays;
    private int maxHours;
    private int maxMinutesSeconds;
    private int maxLengthString;
    private String dateFormat;
}
