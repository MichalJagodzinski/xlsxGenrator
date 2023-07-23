package com.executor.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class DataConfig {
    private List<String> constraints;
    private Integer amount;
    private String mode;
}
