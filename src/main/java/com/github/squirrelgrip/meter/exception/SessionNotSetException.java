package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class SessionNotSetException extends MeterReaderException {
    public SessionNotSetException(Map<ContextKey, Object> context) {
        super("Session not set", context);
    }
}
