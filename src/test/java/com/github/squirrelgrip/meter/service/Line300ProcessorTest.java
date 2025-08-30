package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.domain.MeterReading;
import com.github.squirrelgrip.meter.exception.InvalidDateFormatException;
import com.github.squirrelgrip.meter.exception.InvalidVersionException;
import com.github.squirrelgrip.meter.exception.NMINotSetException;
import com.github.squirrelgrip.meter.exception.VersionNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static com.github.squirrelgrip.meter.domain.ContextKey.*;
import static com.github.squirrelgrip.meter.domain.Version.NEM12;
import static com.github.squirrelgrip.meter.domain.Version.NEM13;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Line300ProcessorTest {
    private Line300Processor testSubject = new Line300Processor();
    private HashMap<ContextKey, Object> context;

    @Mock
    private DatabaseSession databaseSession;

    @BeforeEach
    public void beforeEach() {
        context = new HashMap<>();
        context.put(LINE_NUMBER, 2);
        context.put(VERSION, NEM12);
        context.put(NMI, "1234567890");
        context.put(INTERVAL_LENGTH, 30);
        context.put(SESSION, databaseSession);
    }

    @Test
    public void process_Given300RecordAndNmiNotSet() {
        context.remove(NMI);

        NMINotSetException exception = assertThrows(NMINotSetException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("NMI not set");
    }

    @Test
    public void process_Given300RecordAndVersionNotSet() {
        context.remove(VERSION);

        VersionNotSetException exception = assertThrows(VersionNotSetException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("Version not set");
    }

    @Test
    public void process_Given300RecordAndVersionNotNEM12() {
        context.put(VERSION, NEM13);

        InvalidVersionException exception = assertThrows(InvalidVersionException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("Record 300 cannot be used in NEM13");
    }

    @Test
    public void process() {
        testSubject.process("300,20050301,0,0,0,0,0,0,0,0,0,0,0,0,0.154,0.460,0.770,1.003,1.059,1.750,1.423,1.200,0.980,1.111,0.800,1.403,1.145,1.173,1.065,1.187,0.900,0.998,0.768,1.432,0.899,1.211,0.873,0.786,1.504,0.719,0.817,0.780,0.709,0.700,0.565,0.655,0.543,0.786,0.430,0.432,A,,,20050310121004, ".split(","), context);

        verify(databaseSession, times(48)).save(isA(MeterReading.class));
    }

    @Test
    public void process_Given200RecordAndDataFormatNotValid() {
        InvalidDateFormatException exception = assertThrows(InvalidDateFormatException.class, () -> testSubject.process("300,050301,0,0,0,0,0,0,0,0,0,0,0,0,0.154,0.460,0.770,1.003,1.059,1.750,1.423,1.200,0.980,1.111,0.800,1.403,1.145,1.173,1.065,1.187,0.900,0.998,0.768,1.432,0.899,1.211,0.873,0.786,1.504,0.719,0.817,0.780,0.709,0.700,0.565,0.655,0.543,0.786,0.430,0.432,A,,,20050310121004, ".split(","), context));
        assertThat(exception.getMessage()).isEqualTo("Invalid date format");
        assertThat(exception.getCause().getMessage()).isEqualTo("Text '050301' could not be parsed at index 6");
    }

}