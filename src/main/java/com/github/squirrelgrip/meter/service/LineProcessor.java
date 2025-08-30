package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public interface LineProcessor {
    void preProcess(Map<ContextKey, Object> context);
    void process(String[] tokens, Map<ContextKey, Object> context);
}
