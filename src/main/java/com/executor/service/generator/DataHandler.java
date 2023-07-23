package com.executor.service.generator;

import com.executor.domain.Config;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface DataHandler {
    void generate(Config config, XSSFWorkbook workbook);
}
