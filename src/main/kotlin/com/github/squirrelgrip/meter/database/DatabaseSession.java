package com.github.squirrelgrip.meter.database;

import com.github.squirrelgrip.meter.entity.MeterReading;

public interface DatabaseSession extends AutoCloseable {
    void start();
    void commit();
    void rollback();

    void save(MeterReading meterReading);
    Long count();

    boolean isTransactionActive();
}
