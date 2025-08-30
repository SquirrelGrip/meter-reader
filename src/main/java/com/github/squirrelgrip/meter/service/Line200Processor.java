package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.domain.Version;
import com.github.squirrelgrip.meter.exception.InvalidIntervalLengthException;
import com.github.squirrelgrip.meter.exception.InvalidRecordException;
import com.github.squirrelgrip.meter.exception.InvalidVersionException;
import com.github.squirrelgrip.meter.exception.VersionNotSetException;

import java.util.Map;
import java.util.Set;

import static com.github.squirrelgrip.meter.domain.ContextKey.*;

public class Line200Processor implements LineProcessor {
    private static final Set<Integer> VALID_INTERVAL_LENGTH = Set.of(5, 15, 30);

    @Override
    public void preProcess(Map<ContextKey, Object> context) {
        if (!context.containsKey(ContextKey.VERSION)) {
            throw new VersionNotSetException(context);
        }
        if (context.get(ContextKey.VERSION) != Version.NEM12) {
            throw new InvalidVersionException("200", context);
        }
        context.remove(NMI);
        context.remove(INTERVAL_LENGTH);
    }

    @Override
    public void process(String[] tokens, Map<ContextKey, Object> context) {
        if (tokens.length != 10) {
            throw new InvalidRecordException(context);
        }
        String nmi = tokens[1];
        Integer intervalLength = Integer.valueOf(tokens[8]);
        if (!VALID_INTERVAL_LENGTH.contains(intervalLength)) {
            throw new InvalidIntervalLengthException(context);
        }

        context.put(NMI, nmi);
        context.put(INTERVAL_LENGTH, intervalLength);
    }
}
