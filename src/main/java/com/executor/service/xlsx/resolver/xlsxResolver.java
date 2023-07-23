package com.executor.service.xlsx.resolver;

import com.executor.config.DataFunctions;
import com.executor.config.DataXlsx;
import com.executor.config.DataOperators;
import com.executor.config.DataTypeConfig;
import com.executor.service.xlsx.framework.ValType;
import com.executor.service.xlsx.resolver.elements.Arg;
import com.executor.service.xlsx.resolver.elements.SheetColumn;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class xlsxResolver {
    @Autowired
    private final DataOperators dataOperators;
    @Autowired
    private final DataFunctions dataFunctions;
    @Autowired
    private final DataTypeConfig dataTypeConfig;
    @Autowired
    private final DataXlsx dataXlsx;
    @Getter
    private final List<Arg> argList = new ArrayList<>();
    @Getter
    private String operator;
    @Getter
    private String function;
    private String[] leftHandSideOperands;
    private String rightHandSideOperands;

    public void resolve(String xlsx) {
        resetResolver();
        resolveOperator(xlsx);
        resolveLeftRightHandSides(xlsx);
        resolveFunc();
        resolveFuncArgs();
    }

    private void resetResolver() {
        operator = null;
        function = null;
        leftHandSideOperands = null;
        rightHandSideOperands = null;
        argList.clear();
    }

    private void resolveOperator(String xlsx) {
        for (String supportedOperator : dataOperators.getSupportedOperators())
            if (xlsx.contains(supportedOperator)) {
                operator = supportedOperator;
                return;
            }
        throw new UnsupportedOperationException("Unsupported operator in xlsx: " + rightHandSideOperands);
    }

    private void resolveLeftRightHandSides(String xlsx) {
        String[] operands = xlsx.split(operator);
        leftHandSideOperands = operands[0].replaceAll(dataXlsx.getStringQuote(), "").split("\\.");
        rightHandSideOperands = operands[1];
    }

    public Predicate<SheetColumn> checkIfSheetColumnApplicable() {
        return (sheetColumn) -> sheetColumn.getSheetName().equals(leftHandSideOperands[0])
                && sheetColumn.getColumnName().equals(leftHandSideOperands[1]);
    }

    private void resolveFunc() {
        for (String supportedFunction : dataFunctions.getSupportedFunctions())
            if (rightHandSideOperands.contains(supportedFunction)) {
                function = supportedFunction;
                return;
            }
        throw new UnsupportedOperationException("Unsupported operator in xlsx: " + rightHandSideOperands);
    }

    private void resolveFuncArgs() {
        String[] args = rightHandSideOperands.replace(function, "")
                .replace(dataXlsx.getFunctionOpenBracket(), "")
                .replace(dataXlsx.getFunctionCloseBracket(), "").split(",");
        boolean processingVector = false, isFirst, processingVectorInit;
        ValType valType = null;
        Arg newArg = null;
        for (String arg : args) {
            String[] parts = arg.split(dataXlsx.getStringQuote() + dataXlsx.getSeparator() + dataXlsx.getStringQuote());
            if (parts.length == 2
                    && arg.contains(dataXlsx.getSeparator())
                    && parts[0].trim().startsWith(dataXlsx.getStringQuote()) //TODO: implement trim in the one place
                    && parts[1].trim().endsWith(dataXlsx.getStringQuote())) //TODO: implement trim in the one place
                valType = ValType.SHEET_COL;

            if (valType != ValType.SHEET_COL)
                arg = StringUtils.deleteWhitespace(arg); // not to remove the whitespaces from the column names

            if (ValType.XSSF_WORKBOOK.name().equals(arg))
                valType = ValType.XSSF_WORKBOOK;

            processingVectorInit = arg.contains(dataXlsx.getVectorOpenBracket());
            if (processingVectorInit || processingVector) {
                if (processingVectorInit)
                    newArg = new Arg();
                isFirst = !processingVector;
                processingVector = !arg.contains(dataXlsx.getVectorCloseBracket());
                arg = arg.replace(dataXlsx.getVectorOpenBracket(), "")
                        .replace(dataXlsx.getVectorCloseBracket(), "");
                newArg = resolveDataTypeVector(arg, newArg, isFirst);
            } else if (valType == ValType.SHEET_COL)
                resolveDataTypeSheetCol(parts, ValType.SHEET_COL);
            else if (valType == ValType.XSSF_WORKBOOK)
                resolveDataTypeXSSFWorkbook(arg, ValType.XSSF_WORKBOOK);
            else
                resolveDataTypeAtomic(arg, ValType.ATOMIC);
            valType = null;
        }
    }

    private Arg resolveDataTypeSheetCol(String[] parts, ValType valType) {
        Arg newArg = Arg.builder().valType(ValType.SHEET_COL).type(dataTypeConfig.getStringType())
                .sheetColumn(
                        SheetColumn.builder().sheetName(parts[0].replaceAll(dataXlsx.getStringQuote().trim(), ""))  //TODO: implement trim in the one place
                                .columnName(parts[1].replaceAll(dataXlsx.getStringQuote(), "").trim()).build()).build();  //TODO: implement trim in the one place
        if (valType == ValType.SHEET_COL) //TODO: check if required
            argList.add(newArg);
        return newArg;
    }

    private Arg resolveDataTypeXSSFWorkbook(String arg, ValType valType) {
        Arg newArg = Arg.builder().valType(ValType.XSSF_WORKBOOK).type(dataTypeConfig.getStringType()).build();
        argList.add(newArg);
        return newArg;
    }

    private Arg resolveDataTypeAtomic(String arg, ValType valType) {
        Arg newArg;
        if (arg.contains(dataXlsx.getStringQuote()))
            newArg = Arg.builder().valType(valType).type(dataTypeConfig.getStringType()).atomic(valType == ValType.ATOMIC ? arg.replace(dataXlsx.getStringQuote(), "") : null).build(); //TODO: check valType == ValType.ATOMIC in .atomic()
        else if (arg.contains(dataXlsx.getDatetimeSeparator()))
            newArg = Arg.builder().valType(valType).type(dataTypeConfig.getDatetimeType()).atomic(valType == ValType.ATOMIC ? arg.replace(dataXlsx.getStringQuote(), "") : null).build();
        else if (arg.contains(dataXlsx.getSeparator()) && NumberUtils.isCreatable(arg))
            newArg = Arg.builder().valType(valType).type(dataTypeConfig.getDoubleType()).atomic(valType == ValType.ATOMIC ? arg : null).build();
        else if (!arg.contains(dataXlsx.getSeparator()) && StringUtils.isNumeric(arg))
            newArg = Arg.builder().valType(valType).type(dataTypeConfig.getIntegerType()).atomic(valType == ValType.ATOMIC ? arg : null).build();
        else
            throw new UnsupportedOperationException("Unsupported data type in xlsx: " + arg);
        if (valType == ValType.ATOMIC) //TODO: check if required
            argList.add(newArg);
        return newArg;
    }

    private Arg resolveDataTypeVector(String arg, Arg newArg, boolean isFirst) {
        if (isFirst)
            newArg = resolveDataTypeAtomic(arg, ValType.VECTOR);
        if (arg.contains(dataXlsx.getStringQuote()))
            newArg.getVector().add(arg.replace(dataXlsx.getStringQuote(), ""));
        else if (arg.contains(dataXlsx.getDatetimeSeparator()))
            newArg.getVector().add(arg.replace(dataXlsx.getStringQuote(), ""));
        else if (arg.contains(dataXlsx.getSeparator()) && NumberUtils.isCreatable(arg)) //TODO: . move to props
            newArg.getVector().add(arg);
        else if (!arg.contains(dataXlsx.getSeparator()) && StringUtils.isNumeric(arg))
            newArg.getVector().add(arg);
        else
            throw new UnsupportedOperationException("Unsupported data type in xlsx: " + arg);
        if (isFirst)
            argList.add(newArg);
        return newArg;
    }
}

