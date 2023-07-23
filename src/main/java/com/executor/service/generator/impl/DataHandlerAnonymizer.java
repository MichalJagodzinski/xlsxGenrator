package com.executor.service.generator.impl;

import com.executor.domain.Config;
import com.executor.service.generator.DataHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataHandlerAnonymizer implements DataHandler {
    @Override
    public void generate(Config config, XSSFWorkbook workbook) {
        System.out.println("Anonymizer"); //TODO: test implementation
    }
}
