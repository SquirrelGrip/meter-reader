package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class VersionAlreadySetException(context: MeterContext) :
    MeterReaderException("Version is already set", context)
