package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class UnknownVersionException extends MeterReaderException {
    public UnknownVersionException(String version, Map<ContextKey, Object> context) {
        super("Unknown Version: " + version, context);
    }
}
