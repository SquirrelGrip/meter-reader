package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.VersionNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static com.github.squirrelgrip.meter.domain.ContextKey.*;
import static com.github.squirrelgrip.meter.domain.Version.NEM12;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Line900ProcessorTest {
    private Line900Processor testSubject = new Line900Processor();
    private HashMap<ContextKey, Object> context;
    @Mock
    private DatabaseSession databaseSession;

    @BeforeEach
    public void beforeEach() {
        context = new HashMap<>();
        context.put(LINE_NUMBER, 2);
        context.put(VERSION, NEM12);
        context.put(SESSION, databaseSession);
    }

    @Test
    public void process_Given900RecordAndVersionNotSet() {
        context.remove(VERSION);

        VersionNotSetException exception = assertThrows(VersionNotSetException.class, () -> testSubject.preProcess(context));
        assertThat(exception.getMessage()).isEqualTo("Version not set");
    }

    @Test
    public void preProcess() {
        context.put(NMI, "abcd");
        context.put(INTERVAL_LENGTH, 5);

        testSubject.preProcess(context);

        assertThat(context).doesNotContainKey(NMI);
        assertThat(context).doesNotContainKey(INTERVAL_LENGTH);
    }

    @Test
    public void process() {
        testSubject.process("900".split(","), context);

        assertThat(context).doesNotContainKey(VERSION);
        verify(databaseSession).commit();
    }

}