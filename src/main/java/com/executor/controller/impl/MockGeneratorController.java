package com.executor.controller.impl;

import com.executor.controller.MockGeneratorControllerApi;
import com.executor.domain.Config;
import com.executor.service.generator.impl.XlsxDocumentGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MockGeneratorController implements MockGeneratorControllerApi {
    private final XlsxDocumentGenerator xlsxDocumentGenerator;

    @Override
    public ResponseEntity generateMock(Config config) throws IOException {
        return ResponseEntity.ok(xlsxDocumentGenerator.generate(config));
    }
}