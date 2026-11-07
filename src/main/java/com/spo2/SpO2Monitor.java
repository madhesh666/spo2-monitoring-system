
package com.spo2;

public class SpO2Monitor {

    public String classifySpO2(int spo2) {

        if (spo2 < 0 || spo2 > 100) {
            return "INVALID";
        }

        if (spo2 < 90) {
            return "CRITICAL";
        }

        if (spo2 < 95) {
            return "WARNING";
        }

        return "NORMAL";
    }

    public String generateWarning(int spo2) {

        String status = classifySpO2(spo2);

        switch (status) {
            case "CRITICAL":
                return "Critical reading. Recheck measurement.";

            case "WARNING":
                return "Low reading. Recheck measurement.";

            case "INVALID":
                return "Invalid SpO2 reading.";

            default:
                return "Reading is in the configured demo range.";
        }
    }
}
