package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class VersionNotSetException(context: MeterContext) :
    MeterReaderException("Version not set", context)
