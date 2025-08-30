package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.database.DatabaseSession;
import com.github.squirrelgrip.meter.database.DatabaseSessionFactory;
import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.MeterReaderException;
import com.github.squirrelgrip.meter.exception.TransactionActiveException;
import com.github.squirrelgrip.meter.exception.UnknownException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.github.squirrelgrip.meter.domain.ContextKey.*;

public class MeterReaderService {
    private final LineHandler lineHandler;
    private final ErrorHandler errorHandler;
    private final DatabaseSessionFactory databaseSessionFactory;

    public MeterReaderService(
            LineHandler lineHandler,
            ErrorHandler errorHandler,
            DatabaseSessionFactory databaseSessionFactory
    ) {
        this.lineHandler = lineHandler;
        this.errorHandler = errorHandler;
        this.databaseSessionFactory = databaseSessionFactory;
    }

    public void process(File file) {
        Map<ContextKey, Object> metaData = new HashMap<>();
        metaData.put(FILE_NAME, file.getName());
        process(file, metaData);
    }

    public void process(File file, Map<ContextKey,Object> context) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            process(inputStream, context);
        } catch (IOException e) {
            processException(e, context);
        }
    }

    public void process(InputStream inputStream, Map<ContextKey,Object> context) {
        process(new BufferedReader(new InputStreamReader(inputStream)), context);
    }

    public void process(BufferedReader reader, Map<ContextKey,Object> context) {
        try (DatabaseSession session = databaseSessionFactory.create()) {
            context.put(SESSION, session);
            String line = "";
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                context.put(LINE_NUMBER, lineCount);
                lineHandler.processLine(line, context);
            }
            if (session.isTransactionActive()) {
                throw new TransactionActiveException(context);
            }
        } catch (Exception e) {
            processException(e, context);
        } finally {
            context.clear();
        }
    }

    private void processException(Exception exception, Map<ContextKey,Object> context) {
        if (exception instanceof MeterReaderException) {
            errorHandler.handleException((MeterReaderException)exception);
        } else {
            errorHandler.handleException(new UnknownException(exception, context));
        }

    }
}
