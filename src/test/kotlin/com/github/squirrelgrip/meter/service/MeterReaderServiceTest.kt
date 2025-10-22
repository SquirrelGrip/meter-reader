package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.database.DatabaseSession
import com.github.squirrelgrip.meter.database.DatabaseSessionFactory
import com.github.squirrelgrip.meter.domain.MeterContext
import com.github.squirrelgrip.meter.entity.MeterReading
import com.github.squirrelgrip.meter.exception.UnknownException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException

@ExtendWith(MockitoExtension::class)
class MeterReaderServiceTest {
    private lateinit var testSubject: MeterReaderService

    @Mock
    private lateinit var errorHandler: ErrorHandler

    @Mock
    private lateinit var databaseSessionFactory: DatabaseSessionFactory

    @Mock
    private lateinit var session: DatabaseSession

    @Mock
    private lateinit var mockBufferedReader: BufferedReader

    @BeforeEach
    fun beforeEach() {
        testSubject = MeterReaderService(errorHandler, databaseSessionFactory)

        `when`(databaseSessionFactory.create()).thenReturn(session)
    }

    @Test
    fun process() {
        `when`(session.isTransactionActive).thenReturn(false)
        val inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.txt")!!

        val context: MeterContext = testSubject.createMeterContext("sample.txt")
        testSubject.process(inputStream, context)

        verify(session).start()
        verify(session).close()
        verify(session).commit()
        verify(session, times(384)).save(any(MeterReading::class.java))
        verify(session).isTransactionActive
        verifyNoMoreInteractions(session)
    }

    @Test
    fun process_Missing900Record() {
        `when`(session.isTransactionActive).thenReturn(true)
        val inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("missing900.txt")!!

        val context: MeterContext = testSubject.createMeterContext("sample.txt")
        testSubject.process(inputStream, context)

        verify(session).start()
        verify(session).close()
        verify(session).rollback()
        verify(session, times(384)).save(any(MeterReading::class.java))
        verify(session).isTransactionActive
        verifyNoMoreInteractions(session)
    }

    @Test
    fun process_GivenInputStreamIsEmpty() {
        val inputStream = ByteArrayInputStream("".toByteArray())

        testSubject.process(inputStream)
    }

    @Test
    fun process_GivenInputStreamContainsNoData() {
        val inputStream = ByteArrayInputStream("\n \n \n ".toByteArray())

        testSubject.process(inputStream)
    }

    @Test
    fun process_GivenReadingFromInputStreamThrowsException() {
        doThrow(IOException()).`when`(mockBufferedReader).readLine()

        testSubject.process(mockBufferedReader)

        verify(errorHandler).handleException(any(UnknownException::class.java))
    }
}
