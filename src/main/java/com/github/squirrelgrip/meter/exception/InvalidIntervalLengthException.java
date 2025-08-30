package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class InvalidIntervalLengthException extends MeterReaderException {
    public InvalidIntervalLengthException(Map<ContextKey, Object> context) {
        super("Interval Length must be 5, 15 or 30", context);
    }
}
