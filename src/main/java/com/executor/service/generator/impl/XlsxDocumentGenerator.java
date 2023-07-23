package com.executor.service.generator.impl;

import com.executor.config.ModeFactory;
import com.executor.domain.Config;
import com.executor.service.generator.DocumentGenerator;
import com.executor.service.utils.impl.DataComparator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class XlsxDocumentGenerator implements DocumentGenerator {
    @Autowired
    private ModeFactory modeFactory;
    @Value("${app.generation.file-location}")
    private String generationFileLocation;

    @Value("${app.generation.file-name}")
    private String fileName;

    @Override
    public String generate(Config config) throws IOException {
        prepareInputConfig(config);
        XSSFWorkbook workbook = generateStructure(config);
        workbook = generateData(config, workbook);
        return saveToFile(workbook);
    }

    private void prepareInputConfig(Config config) {
        config.getConfig().sort(new DataComparator());
    }

    private XSSFWorkbook generateStructure(Config config) {
        XSSFSheet sheet;
        XSSFRow headerRow;
        XSSFWorkbook workbook = new XSSFWorkbook();

        for (int sheetIndex = 0; sheetIndex < config.getConfig().size(); sheetIndex++) {
            sheet = workbook.createSheet(config.getConfig().get(sheetIndex).getDataStructureConfig().getSheetName());
            headerRow = sheet.createRow(config.getConfig().get(sheetIndex).getDataStructureConfig().getHeaderRowIndex());
            int cellNumber = 0;
            for (String headerCell : config.getConfig().get(sheetIndex).getDataStructureConfig().getColumns()) {
                XSSFCell cell = headerRow.createCell(cellNumber);
                cell.setCellValue(!Objects.isNull(headerCell) ? headerCell : "");
                cellNumber++;
            }
        }
        return workbook;
    }

    private XSSFWorkbook generateData(Config config, XSSFWorkbook workbook) {
        for (int sheetIndex = 0; sheetIndex < config.getConfig().size(); sheetIndex++)
            modeFactory.getMode(config.getConfig().get(sheetIndex).getDataConfig().getMode())
                    .generate(config, workbook);
        return workbook;
    }

    private String saveToFile(XSSFWorkbook workbook) throws IOException {
        File file = Paths.get(generationFileLocation + File.separator + fileName).toFile();
        FileOutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);

        fileOut.close();
        workbook.close();
        return file.getAbsolutePath();
    }
}
