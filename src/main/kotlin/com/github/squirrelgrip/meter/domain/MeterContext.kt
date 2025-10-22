package com.github.squirrelgrip.meter.domain

import com.github.squirrelgrip.meter.database.DatabaseSession
import com.github.squirrelgrip.meter.exception.InvalidIntervalLengthException
import com.github.squirrelgrip.meter.exception.InvalidRecordException
import com.github.squirrelgrip.meter.exception.InvalidVersionException
import com.github.squirrelgrip.meter.exception.NMINotSetException
import com.github.squirrelgrip.meter.exception.VersionAlreadySetException
import com.github.squirrelgrip.meter.exception.VersionNotSetException

interface MeterContext {
    val session: DatabaseSession
    val fileName: String
    val lineNumber: Int
    val version: Version?
    val intervalLength: Long?
    val nmi: String?

    val currentMap: Map<ContextKey, Any?>

    fun put(key: ContextKey, value: Any?): Any?
    fun remove(key: ContextKey): Any?

    fun requiresVersionNotSet() {
        if (version != null) {
            throw VersionAlreadySetException(this)
        }
    }

    fun requiresVersion(recordIndicator: String, requiredVersion: Version) {
        if (version == null) {
            throw VersionNotSetException(this)
        }
        if (version != requiredVersion) {
            throw InvalidVersionException(recordIndicator, requiredVersion, this)
        }
    }

    fun requiresTokenLength(tokens: Array<String>, requiredCount: Int) {
        if (tokens.size != requiredCount) {
            throw InvalidRecordException(this)
        }
    }

    fun requiresIntervalLength() {
        if (intervalLength == null) {
            throw InvalidIntervalLengthException(this)
        }
    }

    fun requiresNmi() {
        if(nmi == null) {
            throw NMINotSetException(this)
        }
    }

    fun clone(): MeterContext
}