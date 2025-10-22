package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.exception.MeterReaderException

interface ErrorHandler {
    fun handleException(exception: MeterReaderException?)
}
