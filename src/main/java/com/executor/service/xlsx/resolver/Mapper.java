package com.executor.service.xlsx.resolver;

import com.executor.config.DataTypeConfig;
import com.executor.service.xlsx.framework.Args;
import com.executor.service.xlsx.framework.SheetCol;
import com.executor.service.xlsx.framework.ValType;
import com.executor.service.xlsx.resolver.elements.Arg;
import com.executor.service.xlsx.resolver.elements.SheetColumn;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final DataTypeConfig dataTypeConfig;

    public Args map(List<Arg> argList, XSSFWorkbook xssfWorkbook) {
        Args args = new Args(dataTypeConfig.getSupportedDataTypes());
        for (Arg arg : argList) {
            Object value = arg.getValueByValType();
            if (arg.getValType() == ValType.SHEET_COL)
                value = SheetCol.builder().sheetName(((SheetColumn) value).getSheetName()).columnName(((SheetColumn) value).getColumnName()).build();
            args.setNext(arg.getValType(), arg.getType(), value != ValType.XSSF_WORKBOOK ? value : xssfWorkbook);
        }
        return args;
    }
}
