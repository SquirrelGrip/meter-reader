package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.exception.MeterReaderException;

public interface ErrorHandler {
    void handleException(MeterReaderException exception);
}
