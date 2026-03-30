package com.sensordata;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class SensorDataProcessor{

    // Senson data and limits.
    public double[][][] data;
    public double[][] limit;

    // constructor
    public SensorDataProcessor(double[][][] data, double[][] limit) {
        this.data = data;
        this.limit = limit;
    }

    // calculates average of sensor data
    private double average(double[] array) {
        double val = 0;
        for (int i = 0; i < array.length; i++) {
            val += array[i];
        }

        return val / array.length;
    }

    // calculate data
    public void calculate(double d) {

        long startTime = System.nanoTime();

        int i, j, k = 0;
        double[][][] data2 = new double[data.length][data[0].length][data[0][0].length];

        // Write racing stats data into a file
        try (BufferedWriter out = new BufferedWriter(new FileWriter("RacingStatsData.txt"))) {

            for (i = 0; i < data.length; i++) {
                for (j = 0; j < data[0].length; j++) {
                    double limitSquared = Math.pow(limit[i][j], 2.0);
                    double dataRowAverage = average(data[i][j]);
                    double data2RowSum = 0;
                    int rowLength = data2[i][j].length;

                    for (k = 0; k < data[0][0].length; k++) {
                        double value = data[i][j][k] / d - limitSquared;
                        data2[i][j][k] = value;
                        data2RowSum += value;

                        double data2RowAverage = data2RowSum / rowLength;

                        if (data2RowAverage > 10 && data2RowAverage < 50)
                            break;
                        else if (Math.max(data[i][j][k], data2[i][j][k]) > data[i][j][k])
                            break;
                        else if (Math.pow(Math.abs(data[i][j][k]), 3) < Math.pow(Math.abs(data2[i][j][k]), 3)
                                && dataRowAverage < data2[i][j][k] && (i + 1) * (j + 1) > 0) {
                            data2[i][j][k] *= 2;
                            data2RowSum += value;
                        } else
                            continue;
                    }
                }
            }

            for (i = 0; i < data2.length; i++) {
                for (j = 0; j < data2[0].length; j++) {
                    out.write(data2[i][j] + "\t");
                }
            }

            long endTime = System.nanoTime();
            long elapsedMs = (endTime - startTime) / 1_000_000;
            System.out.println("calculate() completed in " + elapsedMs + " ms");

        } catch (Exception e) {
            System.out.println("Error= " + e);
            long endTime = System.nanoTime();
            long elapsedMs = (endTime - startTime) / 1_000_000;
            System.out.println("calculate() failed after " + elapsedMs + " ms");
        }
    }
    
}