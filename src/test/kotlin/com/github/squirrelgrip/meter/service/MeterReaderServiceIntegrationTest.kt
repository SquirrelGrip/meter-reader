package com.github.squirrelgrip.meter.service

import com.github.squirrelgrip.meter.database.DatabaseSessionFactory
import com.github.squirrelgrip.meter.database.hibernate.HibernateDatabaseSessionFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class MeterReaderServiceIntegrationTest {
    private lateinit var testSubject: MeterReaderService
    private lateinit var sessionFactory: DatabaseSessionFactory

    @BeforeEach
    fun beforeEach() {
        sessionFactory = HibernateDatabaseSessionFactory()
        testSubject = MeterReaderService(
            DefaultErrorHandler(),
            sessionFactory
        )
    }

    @Test
    fun process() {
        testSubject.process(File("src/test/resources/sample.txt"))

        assertThat(sessionFactory.create().count()).isEqualTo(384L)
    }

    @Test
    fun process_WithFileContainingAnError() {
        testSubject.process(File("src/test/resources/error.txt"))

        assertThat(sessionFactory.create().count()).isEqualTo(0L)
    }

    @Test
    fun process_withFileDoesNotExist() {
        testSubject.process(File("unknown.txt"))

        assertThat(sessionFactory.create().count()).isEqualTo(0)
    }
}
