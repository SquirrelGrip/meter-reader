package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.exception.MeterReaderException
import com.github.squirrelgrip.meter.exception.UnknownException

class DefaultErrorHandler : ErrorHandler {
    override fun handleException(exception: MeterReaderException?) {
        if (exception == null) {return}
        val session = exception.context.session
        if (session.isTransactionActive) {
            session.rollback()
        }
        if (exception is UnknownException) {
            println(exception.message)
            println("Cause: " + exception.cause?.message)
        } else {
            println("Error occurred at line ${exception.context.lineNumber} in ${exception.context.fileName}")
            println(exception.message)
        }
    }
}
