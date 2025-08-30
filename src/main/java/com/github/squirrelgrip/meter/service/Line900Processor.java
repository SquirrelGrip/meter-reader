package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.VersionNotSetException;

import java.util.Map;

import static com.github.squirrelgrip.meter.domain.ContextKey.*;

public class Line900Processor implements LineProcessor {
    @Override
    public void preProcess(Map<ContextKey, Object> context) {
        if (!context.containsKey(ContextKey.VERSION)) {
            throw new VersionNotSetException(context);
        }
        context.remove(NMI);
        context.remove(INTERVAL_LENGTH);
    }

    @Override
    public void process(String[] tokens, Map<ContextKey, Object> context) {
        context.remove(VERSION);

        DatabaseSession databaseSession = (DatabaseSession) context.get(SESSION);
        databaseSession.commit();
    }
}
