package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class InvalidDateFormatException(context: MeterContext, cause: Exception?) :
    MeterReaderException("Invalid date format", context, cause)
