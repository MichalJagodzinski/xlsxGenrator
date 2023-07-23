package com.executor.service.generator;

import com.executor.domain.Config;

import java.io.IOException;

public interface DocumentGenerator {
    String generate(Config config) throws IOException;
}
