package com.executor.config;

import com.executor.service.generator.DataHandler;
import com.executor.service.generator.impl.DataHandlerAnonymizer;
import com.executor.service.generator.impl.DataHandlerRandomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModeFactory {
    @Bean(name = "dataHandlerRandomizer")
    @ConditionalOnExpression("'${app.generation.supported-modes}'.contains('randomization')")
    public DataHandlerRandomizer dataHandlerRandomizer() {
        return new DataHandlerRandomizer();
    }

    @Bean(name = "dataHandlerAnonymizer")
    @ConditionalOnExpression("'${app.generation.supported-modes}'.contains('anonymization')")
    public DataHandlerAnonymizer dataHandlerAnonymizer() {
        return new DataHandlerAnonymizer();
    }

    public DataHandler getMode(String mode) {
        switch (mode) {
            case "randomization":
                return dataHandlerRandomizer();
            case "anonymization":
                return dataHandlerAnonymizer();
            default:
                throw new UnsupportedOperationException("Unsupported mode: " + mode); //TODO: add Exception handler, see #23
        }
    }
}
