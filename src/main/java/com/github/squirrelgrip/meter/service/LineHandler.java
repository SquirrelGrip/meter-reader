package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public interface LineHandler {
    void processLine(String line, Map<ContextKey, Object> context);
}
