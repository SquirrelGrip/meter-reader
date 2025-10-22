package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext

class TransactionActiveException(context: MeterContext) :
    MeterReaderException("Transaction is still active", context)
