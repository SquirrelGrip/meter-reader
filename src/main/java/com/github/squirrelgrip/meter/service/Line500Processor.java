package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.domain.Version;
import com.github.squirrelgrip.meter.exception.InvalidVersionException;
import com.github.squirrelgrip.meter.exception.NMINotSetException;
import com.github.squirrelgrip.meter.exception.VersionNotSetException;

import java.util.Map;

public class Line500Processor implements LineProcessor {
    @Override
    public void preProcess(Map<ContextKey, Object> context) {
        if (!context.containsKey(ContextKey.VERSION)) {
            throw new VersionNotSetException(context);
        }
        if (context.get(ContextKey.VERSION) != Version.NEM12) {
            throw new InvalidVersionException("500", context);
        }
        if (!context.containsKey(ContextKey.NMI)) {
            throw new NMINotSetException(context);
        }
    }

    @Override
    public void process(String[] tokens, Map<ContextKey, Object> context) {
        context.remove(ContextKey.NMI);
        context.remove(ContextKey.INTERVAL_LENGTH);
    }
}
