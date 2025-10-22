package com.github.squirrelgrip.meter.domain

import com.github.squirrelgrip.meter.database.DatabaseSession

/**
 *                   <---------------------------
 *                   |        <---------        ^
 *                   |        |        ^        |
 * CREATED ---> 100 -+-> 200 -+-> 300 -+-> 500 -+-> 900 ---> END
 */
open class BaseMeterContext(
    contextMap: Map<ContextKey, Any?>
) : MeterContext {
    constructor(session: DatabaseSession, fileName: String): this(
        mapOf< ContextKey, Any?>(
            ContextKey.SESSION to session,
            ContextKey.FILE_NAME to fileName
        )
    )

    val map: MutableMap<ContextKey, Any?> = contextMap.toMutableMap()

    override val session: DatabaseSession
        get() = map[ContextKey.SESSION] as DatabaseSession
    override val fileName: String
        get() = map[ContextKey.FILE_NAME] as String? ?: ""
    override val version: Version?
        get() = map[ContextKey.VERSION] as Version?
    override val intervalLength: Long?
        get() = when (val v = map[ContextKey.INTERVAL_LENGTH]) {
            is Long -> v
            is Int -> v.toLong()
            is Number -> v.toLong()
            else -> v as Long?
        }
    override val lineNumber: Int
        get() = map[ContextKey.LINE_NUMBER] as Int? ?: 0
    override val nmi: String?
        get() = map[ContextKey.NMI] as String?
    override val currentMap: Map<ContextKey, Any?>
        get() = map.toMap()

   override fun remove(key: ContextKey): Any? =
        map.remove(key)

    override fun clone(): MeterContext =
        BaseMeterContext(currentMap)

    override fun put(key: ContextKey, value: Any?): Any? =
        map.put(key, value)
}
