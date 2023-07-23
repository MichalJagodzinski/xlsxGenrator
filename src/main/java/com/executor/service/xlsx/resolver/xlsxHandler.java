package com.executor.service.xlsx.resolver;

import com.executor.service.xlsx.framework.Val;
import com.executor.service.xlsx.resolver.elements.SheetColumn;
import com.executor.service.xlsx.support.SupportedFunctions;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.Optional;

//'X'.'x'=DICT('5', '10', '15')

@Component
@RequiredArgsConstructor
public class xlsxHandler {
    private final xlsxResolver resolver;
    private final SupportedFunctions supportedFunctions;
    private final Mapper mapper;
    private XSSFWorkbook xssfWorkbook;

    public xlsxHandler init(String xlsx) {
        resolver.resolve(xlsx);
        return this;
    }

    public xlsxHandler init(String xlsx, XSSFWorkbook xssfWorkbook) {
        this.xssfWorkbook = xssfWorkbook;
        resolver.resolve(xlsx);
        return this;
    }

    public Optional<Val> run(SheetColumn sheetColumn) {
        if (resolver.checkIfSheetColumnApplicable().test(sheetColumn)) {
            return Optional.of(supportedFunctions.getSupportedFunctions()
                    .get(resolver.getFunction())
                    .setArgs(mapper.map(resolver.getArgList(), xssfWorkbook))
                    .transform());
        }
        return Optional.empty();
    }
}
