package com.executor.service.generator.impl;

import com.executor.config.ModeFactory;
import com.executor.domain.Config;
import com.executor.service.generator.DataHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The class is not handled by Spring Boot, but it's handled with the
 * factory method in {@link ModeFactory}. Please see the project-doc
 * to learn how to add the new mode in the app.
 *
 * @see ModeFactory
 */
public class DataHandlerAnonymizer implements DataHandler {
    @Override
    public void generate(Config config, XSSFWorkbook workbook) {
        System.out.println("Anonymizer"); //TODO: test implementation
    }
}
