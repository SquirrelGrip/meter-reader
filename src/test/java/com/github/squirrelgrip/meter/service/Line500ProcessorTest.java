package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.InvalidVersionException;
import com.github.squirrelgrip.meter.exception.NMINotSetException;
import com.github.squirrelgrip.meter.exception.VersionNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.github.squirrelgrip.meter.domain.ContextKey.*;
import static com.github.squirrelgrip.meter.domain.Version.NEM12;
import static com.github.squirrelgrip.meter.domain.Version.NEM13;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Line500ProcessorTest {
    private Line500Processor testSubject = new Line500Processor();
    private HashMap<ContextKey, Object> context;

    @BeforeEach
    public void beforeEach() {
        context = new HashMap<>();
        context.put(LINE_NUMBER, 2);
        context.put(VERSION, NEM12);
        context.put(NMI, "1234567890");
        context.put(INTERVAL_LENGTH, 30);
    }

    @Test
    public void process_Given500RecordAndNmiNotSet() {
        context.remove(NMI);

        NMINotSetException exception = assertThrows(NMINotSetException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("NMI not set");
    }

    @Test
    public void process_Given500RecordAndVersionNotSet() {
        context.remove(VERSION);

        VersionNotSetException exception = assertThrows(VersionNotSetException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("Version not set");
    }

    @Test
    public void process_Given500RecordAndVersionNotNEM12() {
        context.put(VERSION, NEM13);

        InvalidVersionException exception = assertThrows(InvalidVersionException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("Record 500 cannot be used in NEM13");
    }

    @Test
    public void process() {
        testSubject.process("500,O,S01009,20050310121004, ".split(","), context);
    }

}