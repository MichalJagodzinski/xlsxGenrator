package com.executor.service.utils.impl;

import com.executor.service.utils.HealthService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HealthServiceImpl implements HealthService {
    @Override
    public Map health() {
        return Map.of("STATUS", "UP");
    }
}
