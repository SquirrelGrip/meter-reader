package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.MeterReaderException;
import com.github.squirrelgrip.meter.exception.UnknownException;

public class DefaultErrorHandler implements ErrorHandler {
    @Override
    public void handleException(MeterReaderException exception) {
        System.out.println(exception.getMessage() + " occurred at line " + exception.getContext().get(ContextKey.LINE_NUMBER));
        if (exception instanceof UnknownException) {
            System.out.println("Cause: ");
            exception.getCause().printStackTrace();
        }
    }
}
