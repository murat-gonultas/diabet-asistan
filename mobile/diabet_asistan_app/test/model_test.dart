import 'package:diabet_asistan_app/models.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  test('MealRecord parses backend JSON response', () {
    final meal = MealRecord.fromJson({
      'id': 1,
      'familyId': 10,
      'childId': 20,
      'mealTime': '2026-06-11T18:00:00Z',
      'mealType': 'OTHER',
      'foodName': 'Pasta',
      'description': 'Dinner plate',
      'photoPath': 'local-demo-photo-placeholder.jpg',
      'estimatedCarbsGram': 55.5,
      'finalCarbsGram': null,
      'confidenceLevel': 'MANUAL',
      'status': 'PENDING_PARENT_REVIEW',
      'createdByUserId': 20,
      'reviewedByUserId': null,
    });

    expect(meal.foodName, 'Pasta');
    expect(meal.estimatedCarbsGram, 55.5);
    expect(meal.status, MealStatus.pendingParentReview);
    expect(meal.displayCarbsGram, 55.5);
  });

  test('FamilyRecipe parses carb per serving value', () {
    final recipe = FamilyRecipe.fromJson({
      'id': 1,
      'familyId': 10,
      'name': 'Rice bowl',
      'description': 'Family recipe',
      'totalCarbsGram': 120,
      'servings': 3,
      'carbsPerServing': 40,
      'defaultPortionDescription': '1 bowl',
      'createdByParentId': 30,
      'active': true,
    });

    expect(recipe.name, 'Rice bowl');
    expect(recipe.carbsPerServing, 40);
    expect(recipe.active, isTrue);
  });
}
