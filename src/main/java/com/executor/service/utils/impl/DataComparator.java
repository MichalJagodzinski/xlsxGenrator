package com.executor.service.utils.impl;

import com.executor.domain.Data;

import java.util.Comparator;

public class DataComparator implements Comparator<Data> {
    @Override
    public int compare(Data o1, Data o2) {
        return o1.getDataStructureConfig().getSheetIndex() - o2.getDataStructureConfig().getSheetIndex();
    }
}
