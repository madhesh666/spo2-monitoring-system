
package com.spo2;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Spo2Controller {

    private final Spo2Simulator simulator = new Spo2Simulator();

    @GetMapping("/api/spo2")
    public Map<String, Object> getReading() {

        int[] reading = simulator.getNextReading();
        int spo2 = reading[0];
        int heartRate = reading[1];

        String status = simulator.getStatus(spo2);

        String message;
        switch (status) {
            case "CRITICAL":
                message = "Critical demo reading: recheck measurement.";
                break;
            case "WARNING":
                message = "Low demo reading: recheck measurement.";
                break;
            case "INVALID":
                message = "Invalid reading.";
                break;
            default:
                message = "Reading is in the configured demo range.";
        }

        return Map.of(
            "spo2", spo2,
            "heartRate", heartRate,
            "status", status,
            "message", message,
            "time", LocalDateTime.now().toString(),
            "mode", "DATASET SIMULATION"
        );
    }
}
