package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class InvalidRecordException(context: MeterContext) :
    MeterReaderException("Record has incorrect number of fields", context)
