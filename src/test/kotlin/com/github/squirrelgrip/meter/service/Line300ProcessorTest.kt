package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.database.DatabaseSession
import com.github.squirrelgrip.meter.domain.BaseMeterContext
import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.entity.MeterReading
import com.github.squirrelgrip.meter.domain.Version
import com.github.squirrelgrip.meter.exception.InvalidDateFormatException
import com.github.squirrelgrip.meter.exception.InvalidVersionException
import com.github.squirrelgrip.meter.exception.NMINotSetException
import com.github.squirrelgrip.meter.exception.VersionNotSetException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.time.format.DateTimeParseException

@ExtendWith(MockitoExtension::class)
internal class Line300ProcessorTest {
    private lateinit var testSubject: Line300Processor
    private lateinit var context: MeterContext

    @Mock
    private lateinit var session: DatabaseSession

    @BeforeEach
    fun beforeEach() {
        testSubject = Line300Processor()
        context = BaseMeterContext(session, "")
        context.put(ContextKey.LINE_NUMBER, 2)
        context.put(ContextKey.VERSION, Version.NEM12)
        context.put(ContextKey.NMI, "1234567890")
        context.put(ContextKey.INTERVAL_LENGTH, 30)
        context.put(ContextKey.SESSION, session)
    }

    @Test
    fun process_Given300RecordAndNmiNotSet() {
        context.remove(ContextKey.NMI)

        val exception = assertThrows(NMINotSetException::class.java) {
            testSubject.process(context, 1, "300")
        }
        assertThat(exception.message).isEqualTo("NMI not set")
    }

    @Test
    fun process_Given300RecordAndVersionNotSet() {
        context.remove(ContextKey.VERSION)

        val exception = assertThrows(VersionNotSetException::class.java) {
            testSubject.process(context, 1, "300")
        }
        assertThat(exception.message).isEqualTo("Version not set")
    }

    @Test
    fun process_Given300RecordAndVersionNotNEM12() {
        context.put(ContextKey.VERSION, Version.NEM13)

        val exception =
            assertThrows(InvalidVersionException::class.java) {
                testSubject.process(context, 1, "300")
            }
        assertThat(exception.message).isEqualTo("Record 300 requires version NEM12 but NEM13 is specified")
    }

    @Test
    fun process() {
        testSubject.process(context, 1, "300,20050301,0,0,0,0,0,0,0,0,0,0,0,0,0.154,0.460,0.770,1.003,1.059,1.750,1.423,1.200,0.980,1.111,0.800,1.403,1.145,1.173,1.065,1.187,0.900,0.998,0.768,1.432,0.899,1.211,0.873,0.786,1.504,0.719,0.817,0.780,0.709,0.700,0.565,0.655,0.543,0.786,0.430,0.432,A,,,20050310121004, ")

        verify(session, times(48)).save(ArgumentMatchers.isA<MeterReading?>(MeterReading::class.java))
    }

    @Test
    fun process_Given200RecordAndDataFormatNotValid() {
        val exception = assertThrows(InvalidDateFormatException::class.java) {
            testSubject.process(context, 1, "300,050301,0,0,0,0,0,0,0,0,0,0,0,0,0.154,0.460,0.770,1.003,1.059,1.750,1.423,1.200,0.980,1.111,0.800,1.403,1.145,1.173,1.065,1.187,0.900,0.998,0.768,1.432,0.899,1.211,0.873,0.786,1.504,0.719,0.817,0.780,0.709,0.700,0.565,0.655,0.543,0.786,0.430,0.432,A,,,20050310121004, ")
        }
        assertThat(exception).hasMessage("Invalid date format")
        assertThat(exception).hasCauseInstanceOf(DateTimeParseException::class.java)
        assertThat(exception.cause).hasMessage("Text '050301' could not be parsed at index 6")
    }
}