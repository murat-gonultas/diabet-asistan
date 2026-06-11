package com.murat.diabetasistan.common;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiInfoController {

    @GetMapping("/api/info")
    public Map<String, Object> getApiInfo() {
        return Map.of(
                "name", "Diabet Asistan API",
                "version", "0.1.0",
                "status", "running",
                "insulinDoseCalculation", false,
                "pumpControl", false,
                "medicalTreatmentRecommendations", false,
                "timestamp", Instant.now().toString()
        );
    }
}
