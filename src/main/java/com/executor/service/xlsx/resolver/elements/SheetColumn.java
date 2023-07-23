package com.executor.service.xlsx.resolver.elements;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SheetColumn {
    private String sheetName;
    private String columnName;
}
