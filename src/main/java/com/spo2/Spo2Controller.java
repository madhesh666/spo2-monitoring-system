
package com.spo2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/spo2")
public class Spo2Controller {

    private final SpO2Monitor monitor = new SpO2Monitor();
    private final NtfyService ntfyService;

    public Spo2Controller(NtfyService ntfyService) {
        this.ntfyService = ntfyService;
    }

    private Map<String, Object> latestReading = Map.of(
        "status", "WAITING",
        "message", "Enter your first reading"
    );

    @GetMapping
    public Map<String, Object> getReading() {
        return latestReading;
    }

    // MANUAL INPUT
    @PostMapping
    public Map<String, Object> submitReading(
            @RequestBody Map<String, Integer> input) {

        Integer spo2 = input.get("spo2");
        Integer heartRate = input.get("heartRate");

        if (spo2 == null || heartRate == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "SpO2 and Heart Rate are required"
            );
        }

        if (heartRate < 30 || heartRate > 220) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid Heart Rate input"
            );
        }

        latestReading = createReading(spo2, heartRate);

        if ("CRITICAL".equals(latestReading.get("status"))) {
            ntfyService.sendCriticalAlert(spo2, heartRate);
        }

        return latestReading;
    }

    // CSV DATASET IMPORT
    @PostMapping(value = "/csv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadCSV(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Please select a CSV file"
            );
        }

        String filename = file.getOriginalFilename();

        if (filename == null ||
                !filename.toLowerCase().endsWith(".csv")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Only CSV files are allowed"
            );
        }

        List<Map<String, Object>> readings = new ArrayList<>();

        try {
            String content = new String(
                file.getBytes(),
                java.nio.charset.StandardCharsets.UTF_8
            );

            String[] rows = content.split("\\r?\\n");

            for (int i = 0; i < rows.length; i++) {
                String row = rows[i].trim();

                if (row.isEmpty()) {
                    continue;
                }

                if (i == 0 && row.toLowerCase().contains("spo2")) {
                    continue;
                }

                String[] values = row.split(",");

                if (values.length < 2) {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid CSV format at row " + (i + 1)
                    );
                }

                int spo2 = Integer.parseInt(values[0].trim());
                int heartRate = Integer.parseInt(values[1].trim());

                if (heartRate < 30 || heartRate > 220) {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid heart rate at row " + (i + 1)
                    );
                }

                Map<String, Object> reading =
                    createReading(spo2, heartRate);

                readings.add(reading);
                latestReading = reading;

                if ("CRITICAL".equals(reading.get("status"))) {
                    ntfyService.sendCriticalAlert(spo2, heartRate);
                }
            }

        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "CSV must contain valid numeric values"
            );
        } catch (java.io.IOException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to read CSV file"
            );
        }

        if (readings.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "CSV contains no readings"
            );
        }

        return Map.of(
            "totalRecords", readings.size(),
            "readings", readings,
            "latestReading", latestReading,
            "message", "CSV processed successfully"
        );
    }

    private Map<String, Object> createReading(
            int spo2, int heartRate) {

        String status = monitor.classifySpO2(spo2);
        String message = monitor.generateWarning(spo2);

        return Map.of(
            "spo2", spo2,
            "heartRate", heartRate,
            "status", status,
            "message", message,
            "time", LocalDateTime.now().toString(),
            "mode", "MANUAL / CSV INPUT"
        );
    }
}
