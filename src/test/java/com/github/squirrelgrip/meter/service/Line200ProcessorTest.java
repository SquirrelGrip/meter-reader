package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.github.squirrelgrip.meter.domain.ContextKey.*;
import static com.github.squirrelgrip.meter.domain.Version.NEM12;
import static com.github.squirrelgrip.meter.domain.Version.NEM13;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Line200ProcessorTest {
    private Line200Processor testSubject = new Line200Processor();
    private HashMap<ContextKey, Object> context;

    @BeforeEach
    public void beforeEach() {
        context = new HashMap<>();
        context.put(LINE_NUMBER, 2);
        context.put(VERSION, NEM12);
    }

    @Test
    public void process_Given200RecordAndVersionNotSet() {
        context.remove(VERSION);

        VersionNotSetException exception = assertThrows(VersionNotSetException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("Version not set");
    }

    @Test
    public void process_Given200RecordAndNEM12VersionSet() {
        testSubject.process("200,NEM1201009,E1E2,1,E1,N1,01009,kWh,30,20050610".split(","), context);

        assertThat(context).containsKey(NMI);
        assertThat(context).containsKey(INTERVAL_LENGTH);
        assertThat(context.get(NMI)).isEqualTo("NEM1201009");
        assertThat(context.get(INTERVAL_LENGTH)).isEqualTo(30);
    }

    @Test
    public void process_GivenNew200Record() {
        context.put(NMI, "abcd");
        context.put(INTERVAL_LENGTH, 15);

        testSubject.preProcess(context);

        assertThat(context).doesNotContainKey(NMI);
        assertThat(context).doesNotContainKey(INTERVAL_LENGTH);
    }

    @Test
    public void process_Given200RecordAndTooFewFields() {
        InvalidRecordException exception = assertThrows(InvalidRecordException.class, () -> testSubject.process("200,NEM1201009,E1E2,1,E1,N1,01009,kWh,30".split(","), context));
        assertThat(exception.getMessage()).isEqualTo("Record has incorrect number of fields");
    }

    @Test
    public void process_Given200RecordAndTooManyFields() {
        InvalidRecordException exception = assertThrows(InvalidRecordException.class, () -> testSubject.process("200,NEM1201009,E1E2,1,E1,N1,01009,kWh,30,20050610,A".split(","), context));
        assertThat(exception.getMessage()).isEqualTo("Record has incorrect number of fields");
    }

    // If Version is not NEM12 throw InvalidVersionException
    @Test
    public void process_Given200RecordAndVersionIsNEM13() {
        context.put(VERSION, NEM13);

        InvalidVersionException exception = assertThrows(InvalidVersionException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("Record 200 cannot be used in NEM13");
    }

    // If IntervalLength is not 5, 15 or 30 throw InvalidIntervalLengthException
    @Test
    public void process_Given200RecordAndIntervalLengthIsNotValid() {
        context.put(VERSION, NEM13);

        InvalidIntervalLengthException exception = assertThrows(InvalidIntervalLengthException.class, () -> testSubject.process("200,NEM1201009,E1E2,1,E1,N1,01009,kWh,1,20050610".split(","), context));
        assertThat(exception.getMessage()).isEqualTo("Interval Length must be 5, 15 or 30");
    }

}