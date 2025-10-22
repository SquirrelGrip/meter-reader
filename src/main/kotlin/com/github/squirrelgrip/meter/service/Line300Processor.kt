package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version
import com.github.squirrelgrip.meter.entity.MeterReading
import com.github.squirrelgrip.meter.exception.InvalidDateFormatException
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class Line300Processor(): BaseLineProcessor() {

    override fun process(
        context: MeterContext,
        tokens: Array<String>
    ) {
        context.requiresVersion("300", Version.NEM12)
        context.requiresNmi()
        context.requiresIntervalLength()

        createMeterReadings(context, tokens)
    }

    private fun createMeterReadings(context: MeterContext, tokens: Array<String>) {
        try {
            val date = LocalDate.parse(tokens[1], FORMATTER).atTime(LocalTime.MIDNIGHT)
            val intervalCount: Int = (1440 / context.intervalLength!!).toInt()
            for (i in 0..<intervalCount) {
                context.session.save(createMeterReading(context, date, context.intervalLength!! * i, BigDecimal(tokens[i + 2])))
            }
        } catch (e: DateTimeParseException) {
            throw InvalidDateFormatException(context, e)
        }
    }

    private fun createMeterReading(
        context: MeterContext,
        date: LocalDateTime,
        minutes: Long,
        consumption: BigDecimal
    ): MeterReading =
        MeterReading().also {
            it.nmi = context.nmi
            it.timestamp = date.plusMinutes(minutes)
            it.consumption = consumption
        }

    companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    }
}
