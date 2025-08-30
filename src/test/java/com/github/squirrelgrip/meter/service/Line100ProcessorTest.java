package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.SessionNotSetException;
import com.github.squirrelgrip.meter.exception.UnknownVersionException;
import com.github.squirrelgrip.meter.exception.VersionAlreadySetException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Line100ProcessorTest {
    private Line100Processor testSubject;
    private HashMap<ContextKey, Object> context;

    @Mock
    private DatabaseSession session;

    @BeforeEach
    public void beforeEach() {
        context = new HashMap<>();
        context.put(LINE_NUMBER, 1);
        context.put(SESSION, session);

        testSubject = new Line100Processor();
    }

    @Test
    public void process_Given100RecordAndNEM12Record() {
        testSubject.process("100,NEM12,200506081149,UNITEDDP,NEMMCO".split(","), context);

        assertThat(context).containsKey(VERSION);
        assertThat(context.get(VERSION)).isEqualTo(NEM12);

        verify(session).start();
    }

    @Test
    public void process_Given100RecordAndNEM13Record() {
        testSubject.process("100,NEM13,200506081149,UNITEDDP,NEMMCO".split(","), context);

        assertThat(context).containsKey(VERSION);
        assertThat(context.get(VERSION)).isEqualTo(NEM13);
    }

    @Test
    public void processLine_Given100RecordAndUnknownRecord() {
        assertThrows(UnknownVersionException.class, () -> testSubject.process("100,UNKNOWN,200506081149,UNITEDDP,NEMMCO".split(","), context));

        assertThat(context).doesNotContainKey(VERSION);
    }

    @Test
    public void processLine_GivenDuplicate100Records() {
        context.put(VERSION, NEM12);

        VersionAlreadySetException exception = assertThrows(VersionAlreadySetException.class, () -> testSubject.preProcess(context));
        assertThat(exception).hasMessage("Version is already set");
    }

    @Test
    public void process_Given100RecordAndSessionNotSet() {
        context.remove(SESSION);

        SessionNotSetException exception = assertThrows(SessionNotSetException.class, () -> testSubject.preProcess(context));
        assertThat(exception).hasMessage("Session not set");
    }


}