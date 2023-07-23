package com.executor.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping(value = "/v1/api/health", produces = MediaType.APPLICATION_JSON_VALUE)
public interface HealthControllerApi {
    @GetMapping
    @ResponseBody
    ResponseEntity<Map> health();
}
