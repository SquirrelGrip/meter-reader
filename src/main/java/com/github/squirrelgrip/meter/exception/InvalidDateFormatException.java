package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class InvalidDateFormatException extends MeterReaderException {
    public InvalidDateFormatException(Map<ContextKey, Object > context, Exception cause) {
        super("Invalid date format", context, cause);
    }
}
