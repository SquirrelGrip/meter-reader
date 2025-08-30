package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class InvalidRecordException extends MeterReaderException {
    public InvalidRecordException(Map<ContextKey, Object> context) {
        super("Record has incorrect number of fields", context);
    }
}
