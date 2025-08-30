package com.github.squirrelgrip.meter.exception;

import com.github.squirrelgrip.meter.domain.ContextKey;

import javax.swing.*;
import java.util.Map;

public class TransactionActiveException extends MeterReaderException {
    public TransactionActiveException(Map<ContextKey, Object> context) {
        super("Transaction is still active", context);
    }
}
