package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version

class Line500Processor(): BaseLineProcessor() {
    override fun process(
        context: MeterContext,
        tokens: Array<String>
    ) {
        context.requiresVersion("500", Version.NEM12)
        context.requiresNmi()

        context.remove(ContextKey.NMI)
        context.remove(ContextKey.INTERVAL_LENGTH)
    }
}
