package com.executor.service.xlsx.framework;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SheetCol {
    private String sheetName;
    private String columnName;
}
