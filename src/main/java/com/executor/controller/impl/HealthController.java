package com.executor.controller.impl;

import com.executor.controller.HealthControllerApi;
import com.executor.service.utils.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HealthController implements HealthControllerApi {
    private final HealthService healthService;

    @Override
    public ResponseEntity<Map> health() {
        return ResponseEntity.ok(healthService.health());
    }
}
