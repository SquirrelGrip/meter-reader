package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class UnknownRecordIndicatorException extends MeterReaderException {
    public UnknownRecordIndicatorException(String recordIndicator, Map<ContextKey, Object> metaData) {
        super("Unknown record indicator: " + recordIndicator, metaData);
    }
}
