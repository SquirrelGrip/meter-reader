package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSessionFactory;
import com.github.squirrelgrip.meter.database.hibernate.HibernateDatabaseSessionFactory;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.domain.MeterReading;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.github.squirrelgrip.meter.domain.ContextKey.FILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeterReaderServiceIntegrationTest {
    private MeterReaderService testSubject;

    private LineHandler lineHandler;
    private ErrorHandler errorHandler;
    private DatabaseSessionFactory databaseSessionFactory;

    @BeforeEach
    void beforeEach() {
        databaseSessionFactory = new HibernateDatabaseSessionFactory();
        testSubject = new MeterReaderService(
                new DefaultLineHandler(),
                new DefaultErrorHandler(),
                databaseSessionFactory
        );
    }

    @Test
    void process() {
        testSubject.process(new File("src/test/resources/sample.txt"));

        assertThat(databaseSessionFactory.create().count()).isEqualTo(384L);
    }

    @Test
    void process_WithFileContainingAnError() {
        testSubject.process(new File("src/test/resources/error.txt"));

        assertThat(databaseSessionFactory.create().count()).isEqualTo(0L);
    }

    @Test
    void process_withFileDoesNotExist() {
        testSubject.process(new File("unknown.txt"));

        assertThat(databaseSessionFactory.create().count()).isEqualTo(0);
    }

}
