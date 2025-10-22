package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext

class Line900Processor(): BaseLineProcessor() {

    override fun process(
        context: MeterContext,
        tokens: Array<String>
    ) {
        context.remove(ContextKey.NMI)
        context.remove(ContextKey.INTERVAL_LENGTH)
        context.remove(ContextKey.VERSION)
        context.session.commit()
    }
}
