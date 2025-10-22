package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class UnknownVersionException(version: String?, context: MeterContext) :
    MeterReaderException("Unknown Version: $version", context)
