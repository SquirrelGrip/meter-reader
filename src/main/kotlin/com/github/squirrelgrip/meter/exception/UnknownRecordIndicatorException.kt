package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class UnknownRecordIndicatorException(recordIndicator: String, context: MeterContext) :
    MeterReaderException("Unknown record indicator: $recordIndicator", context)
