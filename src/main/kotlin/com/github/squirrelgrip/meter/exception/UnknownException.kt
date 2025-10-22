package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class UnknownException(cause: Exception, context: MeterContext) :
    MeterReaderException("Unknown Exception", context, cause)
