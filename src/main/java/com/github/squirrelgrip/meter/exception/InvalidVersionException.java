package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class InvalidVersionException extends MeterReaderException {
    public InvalidVersionException(String recordIndicator, Map<ContextKey, Object> metaData) {
        super("Record " + recordIndicator + " cannot be used in " + metaData.get(ContextKey.VERSION), metaData);
    }
}
