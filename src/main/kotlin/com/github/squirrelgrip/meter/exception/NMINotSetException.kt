package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class NMINotSetException(context: MeterContext):
    MeterReaderException("NMI not set", context)
