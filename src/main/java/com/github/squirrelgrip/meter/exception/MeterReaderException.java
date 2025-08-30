package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class MeterReaderException extends RuntimeException {
    private final Map<ContextKey, Object> context;

    public MeterReaderException(String message, Map<ContextKey, Object> context) {
        this(message, context, null);
    }

    public MeterReaderException(String message, Map<ContextKey, Object> context, Exception cause) {
        super(message, cause);
        this.context = Map.copyOf(context); // Make sure we copy the current context as Immutable
    }

    public Map<ContextKey, Object> getContext() {
        return context;
    }
}
