package com.github.squirrelgrip.meter.database

import com.github.squirrelgrip.meter.entity.MeterReading

class NoOpDatabaseSession: DatabaseSession {
    override fun start() { /* no-op */ }
    override fun commit() { /* no-op */ }
    override fun rollback() { /* no-op */ }
    override fun save(meterReading: MeterReading) { /* no-op */ }
    override fun count(): Long = 0L
    override fun isTransactionActive(): Boolean = false
    override fun close() { /* no-op */ }
}
