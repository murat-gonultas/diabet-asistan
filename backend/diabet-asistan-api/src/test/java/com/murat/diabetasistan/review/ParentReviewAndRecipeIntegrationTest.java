package com.murat.diabetasistan.review;

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
class ParentReviewAndRecipeIntegrationTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${local.server.port}")
    private int port;

    @Test
    void parentCanCorrectMealAndCreateFamilyRecipe() throws Exception {
        Long familyId = createFamily();
        Long childId = createUser(familyId, "Child Review Demo", "CHILD");
        Long parentId = createUser(familyId, "Parent Review Demo", "PARENT");
        Long mealId = createMeal(familyId, childId);

        String correctionJson = """
                {
                  "parentId": %d,
                  "reviewStatus": "CORRECTED",
                  "correctedCarbsGram": 70,
                  "comment": "Portion was larger than expected."
                }
                """.formatted(parentId);

        JsonNode review = postJson("/api/meals/" + mealId + "/reviews", correctionJson, 201);

        assertEquals("CORRECTED", review.get("reviewStatus").asText());
        assertEquals(60, review.get("originalCarbsGram").asInt());
        assertEquals(70, review.get("correctedCarbsGram").asInt());
        assertEquals("CORRECTED", review.get("meal").get("status").asText());
        assertEquals(70, review.get("meal").get("finalCarbsGram").asInt());
        assertEquals(parentId, review.get("meal").get("reviewedByUserId").asLong());

        JsonNode correctedMeal = getJson("/api/meals/" + mealId, 200);

        assertEquals("CORRECTED", correctedMeal.get("status").asText());
        assertEquals(70, correctedMeal.get("finalCarbsGram").asInt());

        String recipeJson = """
                {
                  "familyId": %d,
                  "name": "Lentil soup",
                  "description": "Family recipe",
                  "totalCarbsGram": 180,
                  "servings": 6,
                  "defaultPortionDescription": "1 bowl",
                  "createdByParentId": %d
                }
                """.formatted(familyId, parentId);

        JsonNode recipe = postJson("/api/family-recipes", recipeJson, 201);

        assertEquals("Lentil soup", recipe.get("name").asText());
        assertEquals(180, recipe.get("totalCarbsGram").asInt());
        assertEquals(6, recipe.get("servings").asInt());
        assertEquals(30, recipe.get("carbsPerServing").asInt());
        assertTrue(recipe.get("active").asBoolean());

        JsonNode recipes = getJson("/api/family-recipes/family/" + familyId, 200);

        assertTrue(recipes.isArray());
        assertEquals(1, recipes.size());
        assertEquals("Lentil soup", recipes.get(0).get("name").asText());
        assertEquals(30, recipes.get(0).get("carbsPerServing").asInt());
    }

    private Long createFamily() throws Exception {
        JsonNode family = postJson(
                "/api/families",
                """
                {
                  "name": "Review Recipe Demo Family"
                }
                """,
                201
        );

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
        return user.get("id").asLong();
    }

    private Long createMeal(Long familyId, Long childId) throws Exception {
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
        return meal.get("id").asLong();
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
