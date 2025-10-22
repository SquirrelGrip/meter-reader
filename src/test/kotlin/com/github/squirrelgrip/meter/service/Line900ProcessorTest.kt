package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.database.DatabaseSession
import com.github.squirrelgrip.meter.domain.BaseMeterContext
import com.github.squirrelgrip.meter.domain.ContextKey
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.domain.Version
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class Line900ProcessorTest {
    private lateinit var testSubject: Line900Processor
    private lateinit var context: MeterContext

    @Mock
    private lateinit var session: DatabaseSession

    @BeforeEach
    fun beforeEach() {
        testSubject = Line900Processor()
        context = BaseMeterContext(session, "")
        context.put(ContextKey.LINE_NUMBER, 2)
        context.put(ContextKey.VERSION, Version.NEM12)
        context.put(ContextKey.SESSION, session)
    }

    @Test
    fun process_Given() {
        context.put(ContextKey.NMI, "abcd")
        context.put(ContextKey.INTERVAL_LENGTH, 5)

        testSubject.process(context, 1, "500,")

        assertThat(context.currentMap).doesNotContainKey(ContextKey.NMI)
        assertThat(context.currentMap).doesNotContainKey(ContextKey.INTERVAL_LENGTH)
    }

    @Test
    fun process() {
        testSubject.process(context, 1, "900")

        assertThat(context.currentMap).doesNotContainKey(ContextKey.VERSION)

        Mockito.verify(session).commit()
    }
}