package com.github.squirrelgrip.meter.database

interface DatabaseSessionFactory {
    fun create(): DatabaseSession
}
