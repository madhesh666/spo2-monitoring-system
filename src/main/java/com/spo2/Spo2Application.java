
package com.spo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Spo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Spo2Application.class, args);
        System.out.println("OxyCare Started Successfully!");
    }
}
