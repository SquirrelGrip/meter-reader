package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class UnknownException extends MeterReaderException {
    public UnknownException(Exception cause, Map<ContextKey, Object > context) {
        super("Unknown Exception", context, cause);
    }
}
