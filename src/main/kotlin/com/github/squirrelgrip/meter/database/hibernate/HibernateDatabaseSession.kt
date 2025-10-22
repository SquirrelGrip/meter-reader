package com.github.squirrelgrip.meter.database.hibernate

import com.github.squirrelgrip.meter.database.DatabaseSession
import com.github.squirrelgrip.meter.entity.MeterReading
import org.hibernate.Session

class HibernateDatabaseSession(private val session: Session) : DatabaseSession {
    override fun start() {
        session.beginTransaction()
    }

    override fun commit() {
        session.transaction.commit()
    }

    override fun rollback() {
        session.transaction.rollback()
    }

    override fun close() {
        if (session.isOpen && session.transaction.isActive) {
            rollback()
        }
        session.close()
    }

    override fun save(meterReading: MeterReading) {
        session.persist(meterReading)
    }

    override fun count(): Long =
        session.createQuery("select count(*) from MeterReading", Long::class.java).uniqueResult()

    override fun isTransactionActive(): Boolean =
        session.transaction.isActive
}
