package com.github.squirrelgrip.meter.database.hibernate;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.domain.MeterReading;
import org.hibernate.Session;

public class HibernateDatabaseSession implements DatabaseSession {
    private final Session session;

    public HibernateDatabaseSession(Session session) {
        this.session = session;
    }

    @Override
    public void start() {
        session.beginTransaction();
    }

    @Override
    public void commit() {
        session.getTransaction().commit();
    }

    @Override
    public void rollback() {
        session.getTransaction().rollback();
    }

    @Override
    public void close() {
        session.close();
    }

    @Override
    public void save(MeterReading meterReading) {
        session.persist(meterReading);
    }

    @Override
    public Long count() {
        return (Long) session.createQuery("select count(*) from MeterReading").uniqueResult();
    }
}
