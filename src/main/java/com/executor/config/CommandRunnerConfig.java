package com.executor.config;

import com.executor.service.xlsx.resolver.xlsxHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandRunnerConfig implements CommandLineRunner {
    private final xlsxHandler factory;
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${app.generation.supported-modes}")
    private String modes;
    @Value("${app.generation.data-types.supported-data-types}")
    private String[] dataTypes;
    @Value("${app.generation.functions.supported-functions}")
    private String[] functions;

    @Override
    public void run(String... args) {
        logDBConnectionInfo();
    }

    private void logDBConnectionInfo() {
        log.info("xlsx-generator"
                + ", profile: " + activeProfile
                + ", available modes: " + modes
                + ", available data types: " + String.join(",", dataTypes)
                + ", available functions: " + String.join(",", functions));
    }
}
