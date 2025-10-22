package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class InvalidIntervalLengthException(context: MeterContext) :
    MeterReaderException("Interval Length must be 5, 15 or 30", context)
