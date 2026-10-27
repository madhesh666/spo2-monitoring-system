
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
            case "INVALID":
                return "Invalid SpO2 reading. Check sensor.";

            case "CRITICAL":
                return "URGENT: Very low SpO2 reading. Seek medical assessment.";

            case "WARNING":
                return "WARNING: Low SpO2 reading. Recheck the measurement.";

            default:
                return "SpO2 reading is within the configured range.";
        }
    }

    public static void main(String[] args) {

        SpO2Monitor monitor = new SpO2Monitor();

        int[] readings = {98, 94, 88, 105};

        for (int reading : readings) {
            System.out.println("SpO2: " + reading + "%");
            System.out.println("Status: "
                    + monitor.classifySpO2(reading));
            System.out.println("Message: "
                    + monitor.generateWarning(reading));
            System.out.println("--------------------------------");
        }
    }
}
