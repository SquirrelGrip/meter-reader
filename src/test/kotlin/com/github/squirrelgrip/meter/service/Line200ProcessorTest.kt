package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.database.DatabaseSession
import com.github.squirrelgrip.meter.domain.BaseMeterContext
import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version
import com.github.squirrelgrip.meter.exception.InvalidIntervalLengthException
import com.github.squirrelgrip.meter.exception.InvalidRecordException
import com.github.squirrelgrip.meter.exception.InvalidVersionException
import com.github.squirrelgrip.meter.exception.VersionNotSetException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class Line200ProcessorTest {
    private lateinit var testSubject: Line200Processor
    private lateinit var context: MeterContext

    @Mock
    private lateinit var session: DatabaseSession

    @BeforeEach
    fun beforeEach() {
        testSubject = Line200Processor()
        context = BaseMeterContext(session, "")
        context.put(ContextKey.VERSION, Version.NEM12)
    }

    @Test
    fun process_Given200RecordAndVersionNotSet() {
        context.remove(ContextKey.VERSION)

        val exception = assertThrows(VersionNotSetException::class.java) {
            testSubject.process(context, 1, "200")
        }
        assertThat(exception.message).isEqualTo("Version not set")
    }

    @Test
    fun process_Given200RecordAndNEM12VersionSet() {
        testSubject.process(context, 1, "200,NEM1201009,E1E2,1,E1,N1,01009,kWh,30,20050610")

        assertThat(context.currentMap).containsKey(ContextKey.NMI)
        assertThat(context.currentMap).containsKey(ContextKey.INTERVAL_LENGTH)
        assertThat(context.nmi).isEqualTo("NEM1201009")
        assertThat(context.intervalLength).isEqualTo(30)
    }

    @Test
    fun process_Given200RecordAndTooFewFields() {
        val exception = assertThrows(InvalidRecordException::class.java) {
            testSubject.process(context, 1, "200,NEM1201009,E1E2,1,E1,N1,01009,kWh,30")
        }
        assertThat(exception.message).isEqualTo("Record has incorrect number of fields")
    }

    @Test
    fun process_GivenNew200Record() {
        context.put(ContextKey.NMI, "abc")
        context.put(ContextKey.INTERVAL_LENGTH, 15)

        testSubject.process(context, 1, "200,xyz,,,,,,,5,")

        assertThat(context.nmi).isEqualTo("xyz")
        assertThat(context.intervalLength).isEqualTo(5)
    }

    @Test
    fun process_Given200RecordAndTooManyFields() {
        val exception = assertThrows(InvalidRecordException::class.java) {
            testSubject.process(context, 1, "200,NEM1201009,E1E2,1,E1,N1,01009,kWh,30,20050610,A")
        }
        assertThat(exception.message).isEqualTo("Record has incorrect number of fields")
    }

    // If Version is not NEM12 throw InvalidVersionException
    @Test
    fun process_Given200RecordAndVersionIsNEM13() {
        context.put(ContextKey.VERSION, Version.NEM13)

        val exception = assertThrows(InvalidVersionException::class.java) {
            testSubject.process(context, 1, "200")
        }
        assertThat(exception.message).isEqualTo("Record 200 requires version NEM12 but NEM13 is specified")
    }

    // If IntervalLength is not 5, 15 or 30 throw InvalidIntervalLengthException
    @Test
    fun process_Given200RecordAndIntervalLengthIsNotValid() {
        val exception = assertThrows(InvalidIntervalLengthException::class.java) {
            testSubject.process(context, 1, "200,NEM1201009,E1E2,1,E1,N1,01009,kWh,1,20050610")
        }
        assertThat(exception.message).isEqualTo("Interval Length must be 5, 15 or 30")
    }

}