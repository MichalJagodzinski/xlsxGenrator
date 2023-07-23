package com.executor.service.generator.impl;

import com.executor.config.AppConfig;
import com.executor.config.DataTypeConfig;
import com.executor.config.ModeFactory;
import com.executor.domain.Config;
import com.executor.service.generator.DataHandler;
import com.executor.service.xlsx.framework.Val;
import com.executor.service.xlsx.resolver.xlsxHandler;
import com.executor.service.xlsx.resolver.elements.SheetColumn;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * The class is not handled by Spring Boot, but it's handled with the
 * factory method in {@link ModeFactory}. Please see the project-doc
 * to learn how to add the new mode in the app.
 *
 * @see ModeFactory
 */
public class DataHandlerRandomizer implements DataHandler {
    private final Random random = new Random();
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private DataTypeConfig dataTypeConfig;
    @Autowired
    private xlsxHandler factory;

    @Override
    public void generate(Config config, XSSFWorkbook workbook) {
        randomGenerator(config, workbook);
    }

    private void randomGenerator(Config config, XSSFWorkbook workbook) {
        XSSFSheet sheet;

        for (int sheetIndex = 0; sheetIndex < config.getConfig().size(); sheetIndex++) {
            sheet = workbook.getSheetAt(sheetIndex);
            XSSFRow row;
            int step = config.getConfig().get(sheetIndex).getDataStructureConfig().getStep(), startRowIndex = config.getConfig().get(sheetIndex).getDataStructureConfig().getStartRowIndex(), lastIndex = config.getConfig().get(sheetIndex).getDataConfig().getAmount() + (step * config.getConfig().get(sheetIndex).getDataConfig().getAmount()) + startRowIndex, headerRowIndex = config.getConfig().get(sheetIndex).getDataStructureConfig().getHeaderRowIndex();
            List<String> constraints = config.getConfig().get(sheetIndex).getDataConfig().getConstraints();
            for (int notEmptyRow = startRowIndex; startRowIndex < lastIndex; startRowIndex++) {
                if (startRowIndex == notEmptyRow) {
                    row = sheet.createRow(startRowIndex);
                    for (int columnNumber = 0; columnNumber < config.getConfig().get(sheetIndex).getDataStructureConfig().getColumns().size(); columnNumber++) {
                        String columnValue = config.getConfig().get(sheetIndex).getDataStructureConfig().getDataTypes().get(columnNumber);
                        Optional<Val> val = applyJfsmQL(sheet.getSheetName(), sheet.getRow(headerRowIndex).getCell(columnNumber).getStringCellValue(), constraints, workbook);
                        if (dataTypeConfig.getStringType().equals(columnValue))
                            row.createCell(columnNumber).setCellValue(val.isPresent() ? val.get().getAtomic() : (RandomStringUtils.randomAlphabetic(appConfig.getMaxLengthString())));
                        else if (dataTypeConfig.getIntegerType().equals(columnValue))
                            row.createCell(columnNumber).setCellValue(val.map(value -> Integer.parseInt(value.getAtomic())).orElseGet(this::generateInteger));
                        else if (dataTypeConfig.getDoubleType().equals(columnValue))
                            row.createCell(columnNumber).setCellValue(val.map(value -> Double.parseDouble(value.getAtomic())).orElseGet(this::generateDouble));
                        else if (dataTypeConfig.getDatetimeType().equals(columnValue))
                            row.createCell(columnNumber).setCellValue(val.isPresent() ? val.get().getAtomic() : generateDate().format(DateTimeFormatter.ofPattern(appConfig.getDateFormat())));
                        else throw new UnsupportedOperationException("Unsupported data type " + columnValue);
                    }
                    notEmptyRow += (step + 1);
                } else sheet.createRow(startRowIndex); // an empty row
            }
        }
    }

    private Integer generateInteger() {
        return random.nextInt(appConfig.getMaxValueInteger());
    }

    private Double generateDouble() {
        return random.nextDouble() + random.nextInt(appConfig.getMaxValueDouble());
    }

    private LocalDateTime generateDate() {
        return LocalDateTime.now().minus(Period.ofDays((random.nextInt(appConfig.getMaxDays())))).minusHours(random.nextInt(appConfig.getMaxHours())).minusMinutes(random.nextInt(appConfig.getMaxMinutesSeconds())).minusSeconds(random.nextInt(appConfig.getMaxMinutesSeconds()));
    }

    private Optional<Val> applyJfsmQL(String sheetName, String columnName, List<String> constraints, XSSFWorkbook xssfWorkbook) {
        for (String constraint : constraints) {
            Optional<Val> res = factory.init(constraint, xssfWorkbook).run(SheetColumn.builder().sheetName(sheetName).columnName(columnName).build());
            if (res.isPresent()) return res;
        }
        return Optional.empty();
    }
}
