package com.murat.diabetasistan.meal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MealCoreFlowIntegrationTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${local.server.port}")
    private int port;

    @Test
    void createFamilyChildParentAndMeal_thenMealIsPendingParentReview() throws Exception {
        Long familyId = createFamily();
        Long childId = createUser(familyId, "Child Demo", "CHILD");
        createUser(familyId, "Parent Demo", "PARENT");

        String mealJson = """
                {
                  "familyId": %d,
                  "childId": %d,
                  "mealTime": "%s",
                  "mealType": "DINNER",
                  "foodName": "Pasta",
                  "description": "Pasta with tomato sauce",
                  "photoPath": null,
                  "estimatedCarbsGram": 60,
                  "createdByUserId": %d
                }
                """.formatted(familyId, childId, Instant.parse("2026-06-11T18:30:00Z"), childId);

        JsonNode meal = postJson("/api/meals", mealJson, 201);

        assertEquals(familyId, meal.get("familyId").asLong());
        assertEquals(childId, meal.get("childId").asLong());
        assertEquals("Pasta", meal.get("foodName").asText());
        assertEquals(60, meal.get("estimatedCarbsGram").asInt());
        assertEquals("MANUAL", meal.get("confidenceLevel").asText());
        assertEquals("PENDING_PARENT_REVIEW", meal.get("status").asText());

        JsonNode pending = getJson("/api/meals/family/" + familyId + "/pending-review", 200);

        assertTrue(pending.isArray());
        assertEquals(1, pending.size());
        assertEquals("Pasta", pending.get(0).get("foodName").asText());
        assertEquals("PENDING_PARENT_REVIEW", pending.get(0).get("status").asText());
    }

    private Long createFamily() throws Exception {
        JsonNode family = postJson(
                "/api/families",
                """
                {
                  "name": "Demo Family"
                }
                """,
                201
        );

        assertEquals("Demo Family", family.get("name").asText());
        return family.get("id").asLong();
    }

    private Long createUser(Long familyId, String displayName, String role) throws Exception {
        String userJson = """
                {
                  "familyId": %d,
                  "displayName": "%s",
                  "role": "%s",
                  "email": null
                }
                """.formatted(familyId, displayName, role);

        JsonNode user = postJson("/api/users", userJson, 201);

        assertEquals(displayName, user.get("displayName").asText());
        assertEquals(role, user.get("role").asText());

        return user.get("id").asLong();
    }

    private JsonNode postJson(String path, String jsonBody, int expectedStatus) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatus, response.statusCode(), response.body());

        return objectMapper.readTree(response.body());
    }

    private JsonNode getJson(String path, int expectedStatus) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + path))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatus, response.statusCode(), response.body());

        return objectMapper.readTree(response.body());
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }
}
