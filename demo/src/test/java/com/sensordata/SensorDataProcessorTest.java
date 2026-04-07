package com.sensordata;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class SensorDataProcessorTest {

    @Test
    void calculate_coversFirstIfBreakWhenAverageBetween10And50() {
        double[][][] data = {{{30.0}}};
        double[][] limit = {{0.0}};

        SensorDataProcessor processor = new SensorDataProcessor(data, limit);

        assertDoesNotThrow(() -> processor.calculate(1.0));
    }

    @Test
    void calculate_coversSecondElseIfBreakWhenComputedValueExceedsCurrent() {
        double[][][] data = {{{5.0}}};
        double[][] limit = {{0.0}};

        SensorDataProcessor processor = new SensorDataProcessor(data, limit);

        assertDoesNotThrow(() -> processor.calculate(0.5));
    }

    @Test
    void calculate_coversThirdElseIfDoublingPath() {
        double[][][] data = {{{-1.0, -9.0}}};
        double[][] limit = {{1.0}};

        SensorDataProcessor processor = new SensorDataProcessor(data, limit);

        assertDoesNotThrow(() -> processor.calculate(1.0));
    }

    @Test
    void calculate_coversNoBranchActionPath() {
        double[][][] data = {{{1.0}}};
        double[][] limit = {{0.0}};

        SensorDataProcessor processor = new SensorDataProcessor(data, limit);

        assertDoesNotThrow(() -> processor.calculate(1.0));
    }

    @Test
    void calculate_coversFirstIfSecondConditionFalseWhenAverageIsAtLeast50() {
        double[][][] data = {{{60.0}}};
        double[][] limit = {{0.0}};

        SensorDataProcessor processor = new SensorDataProcessor(data, limit);

        assertDoesNotThrow(() -> processor.calculate(1.0));
    }

    @Test
    void calculate_coversCatchBlockOnRuntimeExceptionInsideTry() {
        double[][][] data = {{{1.0}}};
        double[][] limit = null;

        SensorDataProcessor processor = new SensorDataProcessor(data, limit);

        assertDoesNotThrow(() -> processor.calculate(1.0));
    }
}
