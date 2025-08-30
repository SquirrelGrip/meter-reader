package com.github.squirrelgrip.meter.service;

import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultLineHandlerTest {
    private DefaultLineHandler testSubject;

    @BeforeEach
    public void beforeEach() {
        testSubject = new DefaultLineHandler();
    }

}
