package com.github.squirrelgrip.meter.service;

import com.github.squirrelgrip.meter.domain.ContextKey;
import com.github.squirrelgrip.meter.exception.UnknownRecordIndicatorException;

import java.util.Map;

public class DefaultLineHandler implements LineHandler {
    private final Map<String, LineProcessor> processors = Map.of(
            "100", new Line100Processor(),
            "200", new Line200Processor(),
            "300", new Line300Processor(),
            "500", new Line500Processor(),
            "900", new Line900Processor()
    );

    @Override
    public void processLine(String line, Map<ContextKey, Object> context) {
        String[] tokens = line.split(",");
        String recordIndicator = tokens[0];
        LineProcessor lineProcessor = processors.get(recordIndicator);
        if (lineProcessor == null) {
            throw new UnknownRecordIndicatorException(recordIndicator, context);
        }
        lineProcessor.preProcess(context);
        lineProcessor.process(tokens, context);
    }
}
