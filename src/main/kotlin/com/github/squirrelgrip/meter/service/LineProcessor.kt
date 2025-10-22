package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.domain.MeterContext

interface LineProcessor {
    fun process(
        context: MeterContext,
        lineNumber: Int,
        tokens: Array<String>
    )

    fun process(
        context: MeterContext,
        lineNumber: Int,
        line: String
    )
}