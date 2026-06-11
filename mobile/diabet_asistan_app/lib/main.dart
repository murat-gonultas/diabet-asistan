import 'package:flutter/material.dart';

import 'api_client.dart';
import 'app_state.dart';
import 'models.dart';

void main() {
  runApp(const DiabetAsistanApp());
}

class DiabetAsistanApp extends StatefulWidget {
  const DiabetAsistanApp({super.key});

  @override
  State<DiabetAsistanApp> createState() => _DiabetAsistanAppState();
}

class _DiabetAsistanAppState extends State<DiabetAsistanApp> {
  late final AppState appState = AppState(apiClient: DiabetAsistanApiClient());

  int selectedIndex = 0;
  bool loading = false;
  String? errorMessage;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Diabet Asistan',
      theme: ThemeData(
        colorSchemeSeed: Colors.teal,
        useMaterial3: true,
      ),
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Diabet Asistan MVP'),
          actions: [
            SegmentedButton<DemoMode>(
              segments: const [
                ButtonSegment(value: DemoMode.child, label: Text('Child')),
                ButtonSegment(value: DemoMode.parent, label: Text('Parent')),
              ],
              selected: {appState.mode},
              onSelectionChanged: (selection) {
                setState(() => appState.mode = selection.first);
              },
            ),
            const SizedBox(width: 12),
          ],
        ),
        body: SafeArea(child: _buildBody()),
        bottomNavigationBar: NavigationBar(
          selectedIndex: selectedIndex,
          onDestinationSelected: (index) => setState(() => selectedIndex = index),
          destinations: const [
            NavigationDestination(
              icon: Icon(Icons.restaurant),
              label: 'Meals',
            ),
            NavigationDestination(
              icon: Icon(Icons.rate_review),
              label: 'Review',
            ),
            NavigationDestination(
              icon: Icon(Icons.menu_book),
              label: 'Recipes',
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildBody() {
    if (!appState.hasSession) {
      return BootstrapView(
        loading: loading,
        errorMessage: errorMessage,
        onBootstrap: _runAction(appState.bootstrapDemo),
      );
    }

    if (loading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (errorMessage != null) {
      return ErrorPanel(
        message: errorMessage!,
        onRetry: _runAction(appState.refreshAll),
      );
    }

    return switch (selectedIndex) {
      0 => MealsView(
          mode: appState.mode,
          meals: appState.meals,
          onCreateMeal: _showCreateMealDialog,
          onRefresh: _runAction(appState.refreshAll),
        ),
      1 => ParentReviewView(
          pendingMeals: appState.pendingMeals,
          onApprove: (meal) => _runAction(() => appState.approveMeal(meal.id))(),
          onCorrect: _showCorrectMealDialog,
          onRefresh: _runAction(appState.refreshAll),
        ),
      _ => RecipesView(
          recipes: appState.recipes,
          onCreateRecipe: _showCreateRecipeDialog,
          onRefresh: _runAction(appState.refreshAll),
        ),
    };
  }

  VoidCallback _runAction(Future<void> Function() action) {
    return () async {
      setState(() {
        loading = true;
        errorMessage = null;
      });

      try {
        await action();
      } catch (error) {
        errorMessage = error.toString();
      } finally {
        if (mounted) {
          setState(() => loading = false);
        }
      }
    };
  }

  Future<void> _showCreateMealDialog() async {
    final result = await showDialog<CreateMealInput>(
      context: context,
      builder: (context) => const CreateMealDialog(),
    );
    if (result == null) {
      return;
    }
    _runAction(
      () => appState.createMeal(
        foodName: result.foodName,
        carbsGram: result.carbsGram,
        description: result.description,
      ),
    )();
  }

  Future<void> _showCorrectMealDialog(MealRecord meal) async {
    final correctedValue = await showDialog<double>(
      context: context,
      builder: (context) => CorrectMealDialog(meal: meal),
    );
    if (correctedValue == null) {
      return;
    }
    _runAction(
      () => appState.correctMeal(
        mealId: meal.id,
        correctedCarbsGram: correctedValue,
      ),
    )();
  }

  Future<void> _showCreateRecipeDialog() async {
    final result = await showDialog<CreateRecipeInput>(
      context: context,
      builder: (context) => const CreateRecipeDialog(),
    );
    if (result == null) {
      return;
    }
    _runAction(
      () => appState.createRecipe(
        name: result.name,
        totalCarbsGram: result.totalCarbsGram,
        servings: result.servings,
      ),
    )();
  }
}

class BootstrapView extends StatelessWidget {
  const BootstrapView({
    required this.loading,
    required this.errorMessage,
    required this.onBootstrap,
    super.key,
  });

  final bool loading;
  final String? errorMessage;
  final VoidCallback onBootstrap;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: ConstrainedBox(
        constraints: const BoxConstraints(maxWidth: 520),
        child: Card(
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Icon(Icons.health_and_safety, size: 48),
                const SizedBox(height: 16),
                const Text(
                  'Safety-first carbohydrate logging',
                  style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 12),
                const Text(
                  'No insulin dose calculation. No pump control. No treatment recommendation.',
                  textAlign: TextAlign.center,
                ),
                if (errorMessage != null) ...[
                  const SizedBox(height: 16),
                  Text(errorMessage!, style: const TextStyle(color: Colors.red)),
                ],
                const SizedBox(height: 20),
                FilledButton.icon(
                  onPressed: loading ? null : onBootstrap,
                  icon: const Icon(Icons.play_arrow),
                  label: const Text('Create local demo family'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class MealsView extends StatelessWidget {
  const MealsView({
    required this.mode,
    required this.meals,
    required this.onCreateMeal,
    required this.onRefresh,
    super.key,
  });

  final DemoMode mode;
  final List<MealRecord> meals;
  final VoidCallback onCreateMeal;
  final VoidCallback onRefresh;

  @override
  Widget build(BuildContext context) {
    return AppListPage(
      title: 'Meal history',
      emptyText: 'No meals logged yet.',
      onRefresh: onRefresh,
      floatingActionButton: mode == DemoMode.child
          ? FloatingActionButton.extended(
              onPressed: onCreateMeal,
              icon: const Icon(Icons.add),
              label: const Text('Create meal'),
            )
          : null,
      children: meals.map((meal) => MealCard(meal: meal)).toList(),
    );
  }
}

class ParentReviewView extends StatelessWidget {
  const ParentReviewView({
    required this.pendingMeals,
    required this.onApprove,
    required this.onCorrect,
    required this.onRefresh,
    super.key,
  });

  final List<MealRecord> pendingMeals;
  final ValueChanged<MealRecord> onApprove;
  final ValueChanged<MealRecord> onCorrect;
  final VoidCallback onRefresh;

  @override
  Widget build(BuildContext context) {
    return AppListPage(
      title: 'Pending parent review',
      emptyText: 'No meals waiting for parent review.',
      onRefresh: onRefresh,
      children: pendingMeals
          .map(
            (meal) => MealCard(
              meal: meal,
              trailing: Wrap(
                spacing: 8,
                children: [
                  OutlinedButton(
                    onPressed: () => onApprove(meal),
                    child: const Text('Approve'),
                  ),
                  FilledButton(
                    onPressed: () => onCorrect(meal),
                    child: const Text('Correct'),
                  ),
                ],
              ),
            ),
          )
          .toList(),
    );
  }
}

class RecipesView extends StatelessWidget {
  const RecipesView({
    required this.recipes,
    required this.onCreateRecipe,
    required this.onRefresh,
    super.key,
  });

  final List<FamilyRecipe> recipes;
  final VoidCallback onCreateRecipe;
  final VoidCallback onRefresh;

  @override
  Widget build(BuildContext context) {
    return AppListPage(
      title: 'Family recipes',
      emptyText: 'No family recipes yet.',
      onRefresh: onRefresh,
      floatingActionButton: FloatingActionButton.extended(
        onPressed: onCreateRecipe,
        icon: const Icon(Icons.add),
        label: const Text('Add recipe'),
      ),
      children: recipes
          .map(
            (recipe) => Card(
              child: ListTile(
                title: Text(recipe.name),
                subtitle: Text(
                  '${recipe.carbsPerServing.toStringAsFixed(1)} g carbs / serving',
                ),
                trailing: Text('${recipe.totalCarbsGram.toStringAsFixed(1)} g'),
              ),
            ),
          )
          .toList(),
    );
  }
}

class AppListPage extends StatelessWidget {
  const AppListPage({
    required this.title,
    required this.emptyText,
    required this.children,
    required this.onRefresh,
    this.floatingActionButton,
    super.key,
  });

  final String title;
  final String emptyText;
  final List<Widget> children;
  final VoidCallback onRefresh;
  final Widget? floatingActionButton;

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        RefreshIndicator(
          onRefresh: () async => onRefresh(),
          child: ListView(
            padding: const EdgeInsets.all(16),
            children: [
              Text(
                title,
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 16),
              if (children.isEmpty)
                Center(
                  child: Padding(
                    padding: const EdgeInsets.all(32),
                    child: Text(emptyText),
                  ),
                )
              else
                ...children,
            ],
          ),
        ),
        if (floatingActionButton != null)
          Positioned(
            right: 16,
            bottom: 16,
            child: floatingActionButton!,
          ),
      ],
    );
  }
}

class MealCard extends StatelessWidget {
  const MealCard({
    required this.meal,
    this.trailing,
    super.key,
  });

  final MealRecord meal;
  final Widget? trailing;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        leading: const Icon(Icons.restaurant_menu),
        title: Text(meal.foodName),
        subtitle: Text(
          '${meal.status.apiValue} • ${meal.displayCarbsGram.toStringAsFixed(1)} g carbs',
        ),
        trailing: trailing,
      ),
    );
  }
}

class ErrorPanel extends StatelessWidget {
  const ErrorPanel({
    required this.message,
    required this.onRetry,
    super.key,
  });

  final String message;
  final VoidCallback onRetry;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Card(
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Icon(Icons.error_outline, color: Colors.red),
              const SizedBox(height: 12),
              Text(message, textAlign: TextAlign.center),
              const SizedBox(height: 12),
              FilledButton(onPressed: onRetry, child: const Text('Retry')),
            ],
          ),
        ),
      ),
    );
  }
}

class CreateMealDialog extends StatefulWidget {
  const CreateMealDialog({super.key});

  @override
  State<CreateMealDialog> createState() => _CreateMealDialogState();
}

class _CreateMealDialogState extends State<CreateMealDialog> {
  final foodController = TextEditingController();
  final carbsController = TextEditingController();
  final descriptionController = TextEditingController();

  @override
  void dispose() {
    foodController.dispose();
    carbsController.dispose();
    descriptionController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return InputDialog(
      title: 'Create meal',
      fields: [
        TextField(
          controller: foodController,
          decoration: const InputDecoration(labelText: 'Food name'),
        ),
        TextField(
          controller: carbsController,
          decoration: const InputDecoration(labelText: 'Estimated carbs gram'),
          keyboardType: TextInputType.number,
        ),
        TextField(
          controller: descriptionController,
          decoration: const InputDecoration(labelText: 'Description'),
        ),
      ],
      onSubmit: () {
        final carbs = double.tryParse(carbsController.text);
        if (foodController.text.trim().isEmpty || carbs == null) {
          return;
        }
        Navigator.of(context).pop(
          CreateMealInput(
            foodName: foodController.text.trim(),
            carbsGram: carbs,
            description: descriptionController.text.trim(),
          ),
        );
      },
    );
  }
}

class CorrectMealDialog extends StatefulWidget {
  const CorrectMealDialog({required this.meal, super.key});

  final MealRecord meal;

  @override
  State<CorrectMealDialog> createState() => _CorrectMealDialogState();
}

class _CorrectMealDialogState extends State<CorrectMealDialog> {
  final correctedController = TextEditingController();

  @override
  void initState() {
    super.initState();
    correctedController.text = widget.meal.estimatedCarbsGram.toStringAsFixed(1);
  }

  @override
  void dispose() {
    correctedController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return InputDialog(
      title: 'Correct meal carbs',
      fields: [
        Text(widget.meal.foodName),
        TextField(
          controller: correctedController,
          decoration: const InputDecoration(labelText: 'Corrected carbs gram'),
          keyboardType: TextInputType.number,
        ),
      ],
      onSubmit: () {
        final value = double.tryParse(correctedController.text);
        if (value == null) {
          return;
        }
        Navigator.of(context).pop(value);
      },
    );
  }
}

class CreateRecipeDialog extends StatefulWidget {
  const CreateRecipeDialog({super.key});

  @override
  State<CreateRecipeDialog> createState() => _CreateRecipeDialogState();
}

class _CreateRecipeDialogState extends State<CreateRecipeDialog> {
  final nameController = TextEditingController();
  final totalCarbsController = TextEditingController();
  final servingsController = TextEditingController();

  @override
  void dispose() {
    nameController.dispose();
    totalCarbsController.dispose();
    servingsController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return InputDialog(
      title: 'Add family recipe',
      fields: [
        TextField(
          controller: nameController,
          decoration: const InputDecoration(labelText: 'Recipe name'),
        ),
        TextField(
          controller: totalCarbsController,
          decoration: const InputDecoration(labelText: 'Total carbs gram'),
          keyboardType: TextInputType.number,
        ),
        TextField(
          controller: servingsController,
          decoration: const InputDecoration(labelText: 'Servings'),
          keyboardType: TextInputType.number,
        ),
      ],
      onSubmit: () {
        final total = double.tryParse(totalCarbsController.text);
        final servings = double.tryParse(servingsController.text);
        if (nameController.text.trim().isEmpty ||
            total == null ||
            servings == null) {
          return;
        }
        Navigator.of(context).pop(
          CreateRecipeInput(
            name: nameController.text.trim(),
            totalCarbsGram: total,
            servings: servings,
          ),
        );
      },
    );
  }
}

class InputDialog extends StatelessWidget {
  const InputDialog({
    required this.title,
    required this.fields,
    required this.onSubmit,
    super.key,
  });

  final String title;
  final List<Widget> fields;
  final VoidCallback onSubmit;

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(title),
      content: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: fields
              .map(
                (field) => Padding(
                  padding: const EdgeInsets.only(bottom: 12),
                  child: field,
                ),
              )
              .toList(),
        ),
      ),
      actions: [
        TextButton(
          onPressed: Navigator.of(context).pop,
          child: const Text('Cancel'),
        ),
        FilledButton(
          onPressed: onSubmit,
          child: const Text('Save'),
        ),
      ],
    );
  }
}

class CreateMealInput {
  const CreateMealInput({
    required this.foodName,
    required this.carbsGram,
    this.description,
  });

  final String foodName;
  final double carbsGram;
  final String? description;
}

class CreateRecipeInput {
  const CreateRecipeInput({
    required this.name,
    required this.totalCarbsGram,
    required this.servings,
  });

  final String name;
  final double totalCarbsGram;
  final double servings;
}
