package com.executor.service.xlsx.support;

import com.executor.config.DataFunctions;
import com.executor.config.DataTypeConfig;
import com.executor.service.xlsx.framework.FuncExpression;
import com.executor.service.xlsx.framework.SheetCol;
import com.executor.service.xlsx.framework.Val;
import com.executor.service.xlsx.framework.ValType;
import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SupportedFunctions {
    @Autowired
    private DataFunctions dataFunctions;
    @Autowired
    private DataTypeConfig dataTypeConfig;

    @Getter
    private Map<String, FuncExpression> supportedFunctions;

    @PostConstruct
    public void init() {
        supportedFunctions = Map.of(
                dataFunctions.getDict(), FuncExpression.builder().function((args) -> {
                    List<Val> arg = args.getArg(0).getVector();
                    return arg.get(new Random().nextInt(arg.size()));
                }).build(),
                dataFunctions.getRange(), FuncExpression.builder().function((args) -> {
                    int min = Integer.parseInt(args.getArg(0).getAtomic()),
                            max = Integer.parseInt(args.getArg(1).getAtomic());
                    return new Val(args.getSupportedDataTypes()).of(ValType.ATOMIC, "integer",
                            new Random().nextInt(max - min + 1) + min);
                }).build(),
                dataFunctions.getOneOf(), FuncExpression.builder().function((args) -> {
                    SheetCol sheetCol = args.getArg(0).getSheetCol(); // sheet col arg
                    XSSFWorkbook xssfWorkbook = args.getArg(1).getXssfWorkbook(); // XSSFWorkbook arg
                    int headerRowIndex = Integer.parseInt(args.getArg(2).getAtomic()), // headerRowIndex arg
                            startRowIndex = Integer.parseInt(args.getArg(3).getAtomic()), // startRowIndex arg
                            rowsCount = Integer.parseInt(args.getArg(4).getAtomic()), // rowsCount arg
                            columnsCount = Integer.parseInt(args.getArg(5).getAtomic()); // columnsCount arg
                    String referenceDataType = args.getArg(6).getAtomic(); //referenceDataType arg

                    XSSFSheet xssfSheet = Optional.ofNullable(xssfWorkbook.getSheet(sheetCol.getSheetName()))
                            .orElseThrow(() -> new UnsupportedOperationException("Unknown sheet " + sheetCol.getSheetName()));

                    int referenceColumnIndex = -1;
                    for (int headerColIndex = 0; headerColIndex < columnsCount; headerColIndex++)
                        if (sheetCol.getColumnName().equals(xssfSheet.getRow(headerRowIndex).getCell(headerColIndex).getStringCellValue())) {
                            referenceColumnIndex = headerColIndex;
                            break;
                        }
                    if (referenceColumnIndex == -1)
                        throw new UnsupportedOperationException("Unknown column " + sheetCol.getColumnName());

                    List referenceColumn = new ArrayList();
                    for (int rowIndex = startRowIndex; rowIndex < startRowIndex + rowsCount; rowIndex++)
                        referenceColumn.add(xssfSheet.getRow(rowIndex).getCell(referenceColumnIndex).getRawValue()); //TODO: check if the data type should be checked

                    if (dataTypeConfig.getIntegerType().equals(referenceDataType))
                        referenceColumn = (List) referenceColumn.stream().map(e -> (int) Double.parseDouble((String) e)).collect(Collectors.toList()); //TODO: replace that tricky thing...

                    return new Val(args.getSupportedDataTypes())
                            .of(ValType.ATOMIC, referenceDataType, referenceColumn.get(new Random().nextInt(rowsCount)));
                }).build()
        );
    }
}
