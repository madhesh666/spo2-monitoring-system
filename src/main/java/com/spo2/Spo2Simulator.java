
package com.spo2;

public class Spo2Simulator {

    // Demo dataset: SpO2 and heart-rate values
    private final int[][] dataset = {
        {98, 75},
        {97, 78},
        {96, 76},
        {94, 82},
        {92, 85},
        {88, 95},
        {96, 80},
        {99, 72}
    };

    private int index = 0;

    public int[] getNextReading() {
        int[] reading = dataset[index];
        index = (index + 1) % dataset.length;
        return reading;
    }

    public String getStatus(int spo2) {
        if (spo2 < 0 || spo2 > 100) {
            return "INVALID";
        } else if (spo2 < 90) {
            return "CRITICAL";
        } else if (spo2 < 95) {
            return "WARNING";
        }
        return "NORMAL";
    }
}
