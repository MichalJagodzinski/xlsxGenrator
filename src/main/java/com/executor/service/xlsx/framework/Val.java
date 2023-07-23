package com.executor.service.xlsx.framework;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class Val {
    private String[] supportedDataTypes;
    private String dataType;
    @Getter
    private String atomic;
    @Getter
    private List<Val> vector;
    @Getter
    private SheetCol sheetCol;
    @Getter
    private XSSFWorkbook xssfWorkbook;

    public Val(String[] supportedDataTypes) {
        this.supportedDataTypes = supportedDataTypes;
    }

    public static Val empty() {
        return new Val();
    }

    public Val of(ValType valType, String type, Object value) {
        if (!Arrays.asList(supportedDataTypes).contains(type))
            throw new UnsupportedOperationException("Unsupported data type " + type);
        switch (valType) {
            case ATOMIC:
                atomic = String.valueOf(value);
                break;
            case VECTOR:
                vector = (List<Val>) ((List) value).stream()
                        .map(e -> new Val(supportedDataTypes).of(ValType.ATOMIC, type, e)).collect(Collectors.toList());
                break;
            case SHEET_COL:
                sheetCol = (SheetCol) value;
                break;
            case XSSF_WORKBOOK:
                xssfWorkbook = (XSSFWorkbook) value;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported val type " + valType);
        }
        this.dataType = type;
        return this;
    }
}
