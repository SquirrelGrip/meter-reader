package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.database.DatabaseSession
import com.github.squirrelgrip.meter.domain.BaseMeterContext
import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version
import com.github.squirrelgrip.meter.exception.InvalidVersionException
import com.github.squirrelgrip.meter.exception.NMINotSetException
import com.github.squirrelgrip.meter.exception.VersionNotSetException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class Line500ProcessorTest {
    private lateinit var testSubject: Line500Processor
    private lateinit var context: MeterContext

    @Mock
    lateinit var session: DatabaseSession

    @BeforeEach
    fun beforeEach() {
        testSubject = Line500Processor()
        context = BaseMeterContext(session, "")
        context.put(ContextKey.LINE_NUMBER, 2)
        context.put(ContextKey.VERSION, Version.NEM12)
        context.put(ContextKey.NMI, "1234567890")
        context.put(ContextKey.INTERVAL_LENGTH, 30)
    }

    @Test
    fun process_Given500RecordAndNmiNotSet() {
        context.remove(ContextKey.NMI)

        val exception = assertThrows(NMINotSetException::class.java) {
            testSubject.process(context, 1, "500")
        }
        assertThat(exception.message).isEqualTo("NMI not set")
    }

    @Test
    fun process_Given500RecordAndVersionNotSet() {
        context.remove(ContextKey.VERSION)

        val exception = assertThrows(VersionNotSetException::class.java) {
            testSubject.process(context, 1, "500")
        }
        assertThat(exception.message).isEqualTo("Version not set")
    }

    @Test
    fun process_Given500RecordAndVersionNotNEM12() {
        context.put(ContextKey.VERSION, Version.NEM13)

        val exception =
            assertThrows(InvalidVersionException::class.java) {
                testSubject.process(context, 1, "500")
            }
        assertThat(exception.message).isEqualTo("Record 500 requires version NEM12 but NEM13 is specified")
    }

    @Test
    fun process() {
        testSubject.process(context, 1, "500,O,S01009,20050310121004, ")
    }
}