package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.domain.Version;
import com.github.squirrelgrip.meter.exception.SessionNotSetException;
import com.github.squirrelgrip.meter.exception.UnknownVersionException;
import com.github.squirrelgrip.meter.exception.VersionAlreadySetException;

import java.util.Map;

import static com.github.squirrelgrip.meter.domain.ContextKey.SESSION;
import static com.github.squirrelgrip.meter.domain.ContextKey.VERSION;

public class Line100Processor implements LineProcessor {

    @Override
    public void preProcess(Map<ContextKey, Object> context) {
        if (!context.containsKey(ContextKey.SESSION)) {
            throw new SessionNotSetException(context);
        }
        if (context.containsKey(ContextKey.VERSION)) {
            throw new VersionAlreadySetException(context);
        }
    }

    @Override
    public void process(String[] tokens, Map<ContextKey, Object> context) {
        try {
            Version version = Version.valueOf(tokens[1]);
            context.put(VERSION, version);
            DatabaseSession session = (DatabaseSession) context.get(SESSION);
            session.start();
        } catch (IllegalArgumentException e) {
            throw new UnknownVersionException(tokens[1], context);
        }
    }
}
