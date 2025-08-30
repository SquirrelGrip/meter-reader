package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import java.util.Map;

public class NMINotSetException extends MeterReaderException {
    public NMINotSetException(Map<ContextKey, Object> context) {
        super("NMI not set", context);
    }
}
