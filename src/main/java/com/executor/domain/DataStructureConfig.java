package com.executor.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class DataStructureConfig {
    private String sheetName;
    private Integer sheetIndex;
    private Integer headerRowIndex;
    private Integer startRowIndex;
    private Integer step;
    private List<String> columns;
    private List<String> dataTypes;
}
