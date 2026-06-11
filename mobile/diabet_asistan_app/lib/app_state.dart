import 'api_client.dart';
import 'models.dart';

enum DemoMode {
  child,
  parent,
}

class DemoSession {
  const DemoSession({
    required this.family,
    required this.child,
    required this.parent,
  });

  final Family family;
  final AppUser child;
  final AppUser parent;
}

class AppState {
  AppState({required this.apiClient});

  final DiabetAsistanApiClient apiClient;

  DemoMode mode = DemoMode.child;
  DemoSession? session;
  List<MealRecord> meals = <MealRecord>[];
  List<MealRecord> pendingMeals = <MealRecord>[];
  List<FamilyRecipe> recipes = <FamilyRecipe>[];

  bool get hasSession => session != null;

  Future<void> bootstrapDemo() async {
    final family = await apiClient.createFamily('Demo Family');
    final child = await apiClient.createUser(
      familyId: family.id,
      displayName: 'Demo Child',
      role: UserRole.child,
    );
    final parent = await apiClient.createUser(
      familyId: family.id,
      displayName: 'Demo Parent',
      role: UserRole.parent,
    );

    session = DemoSession(family: family, child: child, parent: parent);
    await refreshAll();
  }

  Future<void> refreshAll() async {
    final activeSession = _requireSession();
    meals = await apiClient.getMealsByFamily(activeSession.family.id);
    pendingMeals =
        await apiClient.getPendingMealsByFamily(activeSession.family.id);
    recipes = await apiClient.getFamilyRecipes(activeSession.family.id);
  }

  Future<void> createMeal({
    required String foodName,
    required double carbsGram,
    String? description,
  }) async {
    final activeSession = _requireSession();
    await apiClient.createMeal(
      familyId: activeSession.family.id,
      childId: activeSession.child.id,
      mealType: MealType.other,
      foodName: foodName,
      estimatedCarbsGram: carbsGram,
      createdByUserId: activeSession.child.id,
      description: description,
      photoPath: 'local-demo-photo-placeholder.jpg',
    );
    await refreshAll();
  }

  Future<void> approveMeal(int mealId) async {
    final activeSession = _requireSession();
    await apiClient.reviewMeal(
      mealId: mealId,
      parentId: activeSession.parent.id,
      reviewStatus: ReviewStatus.approved,
      comment: 'Approved in mobile MVP.',
    );
    await refreshAll();
  }

  Future<void> correctMeal({
    required int mealId,
    required double correctedCarbsGram,
  }) async {
    final activeSession = _requireSession();
    await apiClient.reviewMeal(
      mealId: mealId,
      parentId: activeSession.parent.id,
      reviewStatus: ReviewStatus.corrected,
      correctedCarbsGram: correctedCarbsGram,
      comment: 'Corrected in mobile MVP.',
    );
    await refreshAll();
  }

  Future<void> createRecipe({
    required String name,
    required double totalCarbsGram,
    required double servings,
  }) async {
    final activeSession = _requireSession();
    await apiClient.createFamilyRecipe(
      familyId: activeSession.family.id,
      name: name,
      totalCarbsGram: totalCarbsGram,
      servings: servings,
      createdByParentId: activeSession.parent.id,
      description: 'Family recipe created from mobile MVP.',
      defaultPortionDescription: '1 serving',
    );
    await refreshAll();
  }

  DemoSession _requireSession() {
    final activeSession = session;
    if (activeSession == null) {
      throw StateError('Demo session is not initialized.');
    }
    return activeSession;
  }
}
