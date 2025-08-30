package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class VersionNotSetException extends MeterReaderException {
    public VersionNotSetException(Map<ContextKey, Object> context) {
        super("Version not set", context);
    }
}
