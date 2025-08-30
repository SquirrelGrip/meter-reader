package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.database.DatabaseSessionFactory;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.MeterReaderException;
import com.github.squirrelgrip.meter.exception.UnknownException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.github.squirrelgrip.meter.domain.ContextKey.FILE_NAME;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeterReaderServiceTest {
    private MeterReaderService testSubject;

    @Mock
    private LineHandler lineHandler;
    @Mock
    private ErrorHandler errorHandler;

    @Mock
    private DatabaseSessionFactory databaseSessionFactory;
    @Mock
    private DatabaseSession databaseSession;
    @Mock
    private BufferedReader mockBufferedReader;

    @BeforeEach
    void beforeEach() {
        testSubject = new MeterReaderService(lineHandler, errorHandler, databaseSessionFactory);

        when(databaseSessionFactory.create()).thenReturn(databaseSession);
    }

    @Test
    void process() throws Exception {
        when(databaseSession.isTransactionActive()).thenReturn(false);
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.txt");

        Map<ContextKey, Object> context = new HashMap<>();
        context.put(FILE_NAME, "sample.txt");
        testSubject.process(inputStream, context);

        verify(lineHandler, times(14)).processLine(anyString(), eq(context));
        verify(databaseSession).close();
        verifyNoMoreInteractions(databaseSession);
    }

    @Test
    void process_Missing900Record() throws Exception {
        when(databaseSession.isTransactionActive()).thenReturn(true);
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("missing900.txt");

        Map<ContextKey, Object> context = new HashMap<>();
        context.put(FILE_NAME, "sample.txt");
        testSubject.process(inputStream, context);

        verify(lineHandler, times(13)).processLine(anyString(), eq(context));
        verify(databaseSession).close();
        verify(databaseSession).isTransactionActive();
        verifyNoMoreInteractions(databaseSession);
    }

    @Test
    void process_GivenInputStreamIsEmpty() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());

        testSubject.process(inputStream, new HashMap<>());

        verifyNoInteractions(lineHandler);
    }

    @Test
    void process_GivenInputStreamContainsNoData() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n \n \n ".getBytes());

        testSubject.process(inputStream, new HashMap<>());

        verifyNoInteractions(lineHandler);
    }

    @Test
    void process_GivenReadingFromInputStreamThrowsException() throws IOException {
        IOException exception = new IOException();
        when(mockBufferedReader.readLine()).thenThrow(exception);

        HashMap<ContextKey, Object> metaData = new HashMap<>();
        testSubject.process(mockBufferedReader, metaData);

        verifyNoInteractions(lineHandler);
        verify(errorHandler).handleException(isA(UnknownException.class));
    }


}
