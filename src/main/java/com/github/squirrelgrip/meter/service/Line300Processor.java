package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.domain.MeterReading;
import com.github.squirrelgrip.meter.domain.Version;
import com.github.squirrelgrip.meter.exception.InvalidDateFormatException;
import com.github.squirrelgrip.meter.exception.InvalidVersionException;
import com.github.squirrelgrip.meter.exception.NMINotSetException;
import com.github.squirrelgrip.meter.exception.VersionNotSetException;

import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;

public class Line300Processor implements LineProcessor {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public void preProcess(Map<ContextKey, Object> context) {
        if (!context.containsKey(ContextKey.VERSION)) {
            throw new VersionNotSetException(context);
        }
        if (context.get(ContextKey.VERSION) != Version.NEM12) {
            throw new InvalidVersionException("300", context);
        }
        if (!context.containsKey(ContextKey.NMI)) {
            throw new NMINotSetException(context);
        }
    }

    @Override
    public void process(String[] tokens, Map<ContextKey, Object> context) {
        try {
            LocalDateTime date = LocalDate.parse(tokens[1], FORMATTER).atTime(LocalTime.MIDNIGHT);
            Integer intervalLength = (Integer) context.get(ContextKey.INTERVAL_LENGTH);
            String nmi = (String) context.get(ContextKey.NMI);
            DatabaseSession databaseSession = (DatabaseSession) context.get(ContextKey.SESSION);

            int intervalCount = 1440 / intervalLength;
            for (int i = 0; i < intervalCount; i++) {
                LocalDateTime timeStamp = date.plusMinutes((long) intervalLength * i);
                BigDecimal consumption = new BigDecimal(tokens[i + 2]);

                MeterReading meterReading = new MeterReading();
                meterReading.setNmi(nmi);
                meterReading.setTimestamp(timeStamp);
                meterReading.setConsumption(consumption);

                databaseSession.save(meterReading);
            }
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException(context, e);
        }
    }
}
