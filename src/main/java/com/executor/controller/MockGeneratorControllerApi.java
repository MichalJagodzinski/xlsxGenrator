package com.executor.controller;

import com.executor.domain.Config;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping(value = "/v1/api/generate-mock", produces = MediaType.APPLICATION_JSON_VALUE)
public interface MockGeneratorControllerApi {
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity generateMock(@RequestBody Config config) throws IOException;
}
