package com.executor.service.xlsx.resolver.elements;

import com.executor.service.xlsx.framework.ValType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Arg {
    private ValType valType;
    private String type;
    private String atomic;
    @Builder.Default
    private List<String> vector = new ArrayList<>();
    private SheetColumn sheetColumn; //TODO: check if the SheetCol could be used

    public Object getValueByValType() {
        switch (valType) {
            case ATOMIC: return getAtomic();
            case VECTOR: return getVector();
            case SHEET_COL: return getSheetColumn();
            case XSSF_WORKBOOK: return ValType.XSSF_WORKBOOK;
            default: throw new UnsupportedOperationException("Unsupported val type: " + valType);
        }
    }
}
