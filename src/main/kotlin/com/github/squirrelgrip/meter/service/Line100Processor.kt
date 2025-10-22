package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version
import com.github.squirrelgrip.meter.exception.UnknownVersionException

class Line100Processor(): BaseLineProcessor() {
    override fun process(context: MeterContext, tokens: Array<String>) {
        context.requiresVersionNotSet()
        setVersion(context, tokens)
        context.session.start()
    }

    private fun setVersion(context: MeterContext, tokens: Array<String>) {
        try {
            context.put(ContextKey.VERSION, Version.valueOf(tokens[1]))
        } catch (e: IllegalArgumentException) {
            throw UnknownVersionException(tokens[1], context)
        }
    }
}
