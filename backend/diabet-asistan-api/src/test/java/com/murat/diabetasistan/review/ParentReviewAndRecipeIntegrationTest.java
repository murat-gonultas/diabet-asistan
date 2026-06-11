package com.murat.diabetasistan.review;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ParentReviewAndRecipeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        mockMvc.perform(post("/api/meals/{mealId}/reviews", mealId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewStatus").value("CORRECTED"))
                .andExpect(jsonPath("$.originalCarbsGram").value(60))
                .andExpect(jsonPath("$.correctedCarbsGram").value(70))
                .andExpect(jsonPath("$.meal.status").value("CORRECTED"))
                .andExpect(jsonPath("$.meal.finalCarbsGram").value(70))
                .andExpect(jsonPath("$.meal.reviewedByUserId").value(parentId));

        mockMvc.perform(get("/api/meals/{mealId}", mealId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CORRECTED"))
                .andExpect(jsonPath("$.finalCarbsGram").value(70));

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

        mockMvc.perform(post("/api/family-recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recipeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lentil soup"))
                .andExpect(jsonPath("$.totalCarbsGram").value(180))
                .andExpect(jsonPath("$.servings").value(6))
                .andExpect(jsonPath("$.carbsPerServing").value(30))
                .andExpect(jsonPath("$.active").value(true));

        mockMvc.perform(get("/api/family-recipes/family/{familyId}", familyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Lentil soup"))
                .andExpect(jsonPath("$[0].carbsPerServing").value(30));
    }

    private Long createFamily() throws Exception {
        String response = mockMvc.perform(post("/api/families")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Review Recipe Demo Family"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asLong();
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

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asLong();
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

        String response = mockMvc.perform(post("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mealJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asLong();
    }
}
