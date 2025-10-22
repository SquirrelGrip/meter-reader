package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext

abstract class BaseLineProcessor(): LineProcessor {
    override fun process(
        context: MeterContext,
        lineNumber: Int,
        line: String
    ) {
        process(context, lineNumber, line.split(",").toTypedArray())
    }

    override fun process(
        context: MeterContext,
        lineNumber: Int,
        tokens: Array<String>
    ) {
        context.put(ContextKey.LINE_NUMBER, lineNumber)
        process(context, tokens)
    }

    abstract fun process(context: MeterContext, tokens: Array<String>)
}
