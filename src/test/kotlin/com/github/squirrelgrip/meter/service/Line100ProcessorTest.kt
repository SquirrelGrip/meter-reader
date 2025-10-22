package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.database.DatabaseSession
import com.github.squirrelgrip.meter.domain.BaseMeterContext
import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version
import com.github.squirrelgrip.meter.exception.UnknownVersionException
import com.github.squirrelgrip.meter.exception.VersionAlreadySetException
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class Line100ProcessorTest {
    private lateinit var testSubject: Line100Processor
    private lateinit var context: MeterContext

    @Mock
    private lateinit var session: DatabaseSession

    @BeforeEach
    fun beforeEach() {
        testSubject = Line100Processor()
        context = BaseMeterContext(session, "")
        context.put(ContextKey.LINE_NUMBER, 1)
    }

    @Test
    fun process_Given100RecordAndNEM12Record() {
        testSubject.process(context, 1, "100,NEM12,200506081149,UNITEDDP,NEMMCO")

        assertThat(context.currentMap).containsKey(ContextKey.VERSION)
        assertThat(context.version).isEqualTo(Version.NEM12)

        Mockito.verify(session).start()
    }

    @Test
    fun process_Given100RecordAndNEM13Record() {
        testSubject.process(context,1, "100,NEM13,200506081149,UNITEDDP,NEMMCO")

        assertThat(context.currentMap).containsKey(ContextKey.VERSION)
        assertThat(context.version).isEqualTo(Version.NEM13)
    }

    @Test
    fun processLine_Given100RecordAndUnknownRecord() {
        assertThrows(UnknownVersionException::class.java) {
            testSubject.process(context, 1, "100,UNKNOWN,200506081149,UNITEDDP,NEMMCO")
        }

        assertThat(context.currentMap).doesNotContainKey(ContextKey.VERSION)
    }

    @Test
    fun processLine_GivenDuplicate100Records() {
        context.put(ContextKey.VERSION, Version.NEM12)

        val exception = assertThrows(VersionAlreadySetException::class.java) {
            testSubject.process(context, 1, "100")
        }
        assertThat(exception).hasMessage("Version is already set")
    }

}