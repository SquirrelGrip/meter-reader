package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.MeterReaderException;
import com.github.squirrelgrip.meter.exception.UnknownException;

public class DefaultErrorHandler implements ErrorHandler {
    @Override
    public void handleException(MeterReaderException exception) {
        DatabaseSession databaseSession = (DatabaseSession) exception.getContext().get(ContextKey.SESSION);
        if (databaseSession != null && databaseSession.isTransactionActive()) {
            databaseSession.rollback();
        }
        if (exception instanceof UnknownException) {
            System.out.println(exception.getMessage());
            System.out.println("Cause: "  + exception.getCause().getMessage());
        } else {
            System.out.println("Error occurred at line " + exception.getContext().get(ContextKey.LINE_NUMBER) + " in " + exception.getContext().get(ContextKey.FILE_NAME));
            System.out.println(exception.getMessage());
        }
    }
}
