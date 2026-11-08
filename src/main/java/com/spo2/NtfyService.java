
package com.spo2;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class NtfyService {

    private final HttpClient client = HttpClient.newHttpClient();

    public void sendCriticalAlert(int spo2, int heartRate) {
        try {
        	String topic = System.getenv("NTFY_TOPIC");

            if (topic == null || topic.isBlank()) {
                throw new IllegalStateException("NTFY_TOPIC is not configured");
            }

            String message = "OxyCare CRITICAL ALERT\n"
                    + "SpO2: " + spo2 + "%\n"
                    + "Heart Rate: " + heartRate + " BPM\n"
                    + "Please recheck the measurement.";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ntfy.sh/" + topic))
                    .header("Title", "OxyCare Critical Alert")
                    .header("Priority", "urgent")
                    .header("Tags", "warning,heart")
                    .POST(HttpRequest.BodyPublishers.ofString(message))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("ntfy failed: " + response.statusCode());
            }

            System.out.println("OxyCare ntfy alert sent successfully!");

        } catch (Exception e) {
            System.err.println("ntfy alert failed: " + e.getMessage());
        }
    }
}
