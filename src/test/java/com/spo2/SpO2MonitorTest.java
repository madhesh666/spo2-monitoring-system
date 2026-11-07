
package com.spo2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpO2MonitorTest {

    private final SpO2Monitor monitor = new SpO2Monitor();

    @Test
    void shouldClassifyNormalReading() {
        assertEquals("NORMAL", monitor.classifySpO2(98));
    }

    @Test
    void shouldClassifyWarningReading() {
        assertEquals("WARNING", monitor.classifySpO2(94));
    }

    @Test
    void shouldClassifyCriticalReading() {
        assertEquals("CRITICAL", monitor.classifySpO2(88));
    }

    @Test
    void shouldRejectReadingAbove100() {
        assertEquals("INVALID", monitor.classifySpO2(105));
    }

    @Test
    void shouldRejectNegativeReading() {
        assertEquals("INVALID", monitor.classifySpO2(-1));
    }

    @Test
    void shouldClassifyBoundaryAt95AsNormal() {
        assertEquals("NORMAL", monitor.classifySpO2(95));
    }

    @Test
    void shouldClassifyBoundaryAt90AsWarning() {
        assertEquals("WARNING", monitor.classifySpO2(90));
    }

    @Test
    void shouldGenerateCriticalWarning() {
        assertEquals(
            "URGENT: Very low SpO2 reading. Seek medical assessment.",
            monitor.generateWarning(88)
        );
    }
}
