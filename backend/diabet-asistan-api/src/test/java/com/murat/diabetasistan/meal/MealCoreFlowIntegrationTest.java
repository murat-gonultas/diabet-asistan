package com.murat.diabetasistan.meal;

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
class MealCoreFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        mockMvc.perform(post("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mealJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.familyId").value(familyId))
                .andExpect(jsonPath("$.childId").value(childId))
                .andExpect(jsonPath("$.foodName").value("Pasta"))
                .andExpect(jsonPath("$.estimatedCarbsGram").value(60))
                .andExpect(jsonPath("$.confidenceLevel").value("MANUAL"))
                .andExpect(jsonPath("$.status").value("PENDING_PARENT_REVIEW"));

        mockMvc.perform(get("/api/meals/family/{familyId}/pending-review", familyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].foodName").value("Pasta"))
                .andExpect(jsonPath("$[0].status").value("PENDING_PARENT_REVIEW"));
    }

    private Long createFamily() throws Exception {
        String response = mockMvc.perform(post("/api/families")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Demo Family"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Demo Family"))
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
                .andExpect(jsonPath("$.displayName").value(displayName))
                .andExpect(jsonPath("$.role").value(role))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asLong();
    }
}
