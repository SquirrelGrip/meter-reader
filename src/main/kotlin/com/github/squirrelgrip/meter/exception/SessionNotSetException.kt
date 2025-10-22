package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class SessionNotSetException(context: MeterContext) :
    MeterReaderException("Session not set", context)
