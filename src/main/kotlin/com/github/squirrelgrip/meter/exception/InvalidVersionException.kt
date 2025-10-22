package com.github.squirrelgrip.meter.exception

import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version

class InvalidVersionException(recordIndicator: String, requiredVersion: Version, context: MeterContext) :
    MeterReaderException(
        "Record $recordIndicator requires version $requiredVersion but ${context.version} is specified",
        context
    )
