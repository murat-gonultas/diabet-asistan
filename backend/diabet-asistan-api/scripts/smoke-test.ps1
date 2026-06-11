# Diabet Asistan Backend Smoke Test
# Start backend before running:
#   cd backend\diabet-asistan-api
#   .\mvnw.cmd spring-boot:run
#
# Then run from repository root:
#   powershell -ExecutionPolicy Bypass -File .\backend\diabet-asistan-api\scripts\smoke-test.ps1

$ErrorActionPreference = "Stop"

$BaseUrl = "http://localhost:8080"

function Write-Step($message) {
    Write-Host ""
    Write-Host "==> $message" -ForegroundColor Cyan
}

function Assert-Equal($actual, $expected, $message) {
    if ($actual -ne $expected) {
        throw "$message Expected '$expected' but got '$actual'."
    }
}

Write-Step "Checking API info endpoint"

$info = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/info"
Assert-Equal $info.insulinDoseCalculation $false "Safety flag insulinDoseCalculation mismatch."
Assert-Equal $info.pumpControl $false "Safety flag pumpControl mismatch."
Assert-Equal $info.medicalTreatmentRecommendations $false "Safety flag medicalTreatmentRecommendations mismatch."

Write-Step "Creating family"

$family = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/families" -ContentType "application/json" -Body (@{
    name = "Smoke Test Family"
} | ConvertTo-Json)

Write-Host "Family ID: $($family.id)"

Write-Step "Creating child user"

$child = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/users" -ContentType "application/json" -Body (@{
    familyId = $family.id
    displayName = "Smoke Test Child"
    role = "CHILD"
    email = $null
} | ConvertTo-Json)

Write-Host "Child ID: $($child.id)"

Write-Step "Creating parent user"

$parent = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/users" -ContentType "application/json" -Body (@{
    familyId = $family.id
    displayName = "Smoke Test Parent"
    role = "PARENT"
    email = $null
} | ConvertTo-Json)

Write-Host "Parent ID: $($parent.id)"

Write-Step "Creating meal record"

$mealBody = @{
    familyId = $family.id
    childId = $child.id
    mealTime = "2026-06-11T18:30:00Z"
    mealType = "DINNER"
    foodName = "Pasta"
    description = "Pasta with tomato sauce"
    photoPath = $null
    estimatedCarbsGram = 60
    createdByUserId = $child.id
} | ConvertTo-Json

$meal = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/meals" -ContentType "application/json" -Body $mealBody
Assert-Equal $meal.status "PENDING_PARENT_REVIEW" "Meal status mismatch after creation."
Assert-Equal $meal.confidenceLevel "MANUAL" "Meal confidence level mismatch."

Write-Host "Meal ID: $($meal.id)"

Write-Step "Checking pending reviews"

$pending = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/meals/family/$($family.id)/pending-review"
if ($pending.Count -lt 1) {
    throw "Expected at least one pending review."
}

Write-Step "Correcting meal as parent"

$reviewBody = @{
    parentId = $parent.id
    reviewStatus = "CORRECTED"
    correctedCarbsGram = 70
    comment = "Portion was larger than expected."
} | ConvertTo-Json

$review = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/meals/$($meal.id)/reviews" -ContentType "application/json" -Body $reviewBody
Assert-Equal $review.reviewStatus "CORRECTED" "Review status mismatch."
Assert-Equal $review.meal.status "CORRECTED" "Meal status mismatch after correction."

Write-Step "Checking corrected meal"

$correctedMeal = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/meals/$($meal.id)"
Assert-Equal $correctedMeal.status "CORRECTED" "Corrected meal status mismatch."
Assert-Equal ([decimal]$correctedMeal.finalCarbsGram) 70 "Final carbohydrates mismatch."

Write-Step "Creating family recipe"

$recipeBody = @{
    familyId = $family.id
    name = "Lentil soup"
    description = "Family recipe"
    totalCarbsGram = 180
    servings = 6
    defaultPortionDescription = "1 bowl"
    createdByParentId = $parent.id
} | ConvertTo-Json

$recipe = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/family-recipes" -ContentType "application/json" -Body $recipeBody
Assert-Equal $recipe.name "Lentil soup" "Recipe name mismatch."
Assert-Equal ([decimal]$recipe.carbsPerServing) 30 "Carbs per serving mismatch."

Write-Step "Checking family recipes"

$recipes = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/family-recipes/family/$($family.id)"
if ($recipes.Count -lt 1) {
    throw "Expected at least one family recipe."
}

Write-Host ""
Write-Host "Smoke test completed successfully." -ForegroundColor Green
