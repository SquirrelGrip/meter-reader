package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.BaseMeterContext
import com.github.squirrelgrip.meter.domain.MeterContext

open class MeterReaderException(
    message: String,
    meterContext: MeterContext,
    cause: Exception? = null
) : RuntimeException(message, cause) {
    val context: MeterContext = meterContext.clone()
}
