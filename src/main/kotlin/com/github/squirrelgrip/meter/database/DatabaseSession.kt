package com.github.squirrelgrip.meter.database

import com.github.squirrelgrip.meter.entity.MeterReading
import java.lang.AutoCloseable

interface DatabaseSession : AutoCloseable {
    fun start()
    fun commit()
    fun rollback()

    fun save(meterReading: MeterReading?)
    fun count(): Long

    val isTransactionActive: Boolean
}
