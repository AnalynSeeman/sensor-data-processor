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
        double sum = 0;
        for (double value : array) {
            sum += value;
        }

        return sum / array.length;
    }

    // calculate data
    public void calculate(double d) {

        long startTime = System.nanoTime();

        int i, j, k;
        double[][][] data2 = new double[data.length][data[0].length][data[0][0].length];

        // Write racing stats data into a file
        try (BufferedWriter out = new BufferedWriter(new FileWriter("RacingStatsData.txt"))) {

            for (i = 0; i < data.length; i++) {
                for (j = 0; j < data[0].length; j++) {
                    double[] dataRow = data[i][j];
                    double[] data2Row = data2[i][j];
                    double limitSquared = Math.pow(limit[i][j], 2.0);
                    double dataRowAverage = average(dataRow);
                    double data2RowSum = 0;

                    for (k = 0; k < data[0][0].length; k++) {
                        double dataVal = dataRow[k];
                        double data2Val = dataVal / d - limitSquared;
                        data2Row[k] = data2Val;
                        data2RowSum += data2Val;

                        double data2RowAverage = data2RowSum / data2Row.length;

                        if (data2RowAverage > 10 && data2RowAverage < 50)
                            break;
                        else if (Math.max(dataVal, data2Val) > dataVal)
                            break;
                        else if (Math.pow(Math.abs(dataVal), 3) < Math.pow(Math.abs(data2Val), 3)
                                && dataRowAverage < data2Val && (i + 1) * (j + 1) > 0) {
                            data2Val *= 2;
                            data2Row[k] = data2Val;
                            data2RowSum += data2Val / 2;
                        }
                        else
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