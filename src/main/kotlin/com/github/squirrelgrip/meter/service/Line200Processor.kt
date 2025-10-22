package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version
import com.github.squirrelgrip.meter.exception.InvalidIntervalLengthException

class Line200Processor() : BaseLineProcessor() {
    companion object {
        private val VALID_INTERVAL_LENGTH = mutableSetOf<Long?>(5, 15, 30)
    }

    override fun process(
        context: MeterContext,
        tokens: Array<String>
    ) {
        context.requiresVersion("200", Version.NEM12)
        context.requiresTokenLength(tokens, 10)

        setIntervalLength(context, tokens)
        setNmi(context, tokens)
    }

    private fun setNmi(context: MeterContext, tokens: Array<String>) {
        context.put(ContextKey.NMI, tokens[1])
    }

    private fun setIntervalLength(context: MeterContext, tokens: Array<String>) {
        val intervalLength = tokens[8].toLong()
        if (!VALID_INTERVAL_LENGTH.contains(intervalLength)) {
            throw InvalidIntervalLengthException(context)
        }
        context.put(ContextKey.INTERVAL_LENGTH, intervalLength)
    }

}
