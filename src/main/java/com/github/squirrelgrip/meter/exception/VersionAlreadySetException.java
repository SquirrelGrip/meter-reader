package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class VersionAlreadySetException extends MeterReaderException {
    public VersionAlreadySetException(Map<ContextKey, Object> context) {
        super("Version is already set", context);
    }
}
