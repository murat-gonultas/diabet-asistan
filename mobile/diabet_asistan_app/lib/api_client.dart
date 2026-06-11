import 'dart:convert';

import 'package:http/http.dart' as http;

import 'models.dart';

class DiabetAsistanApiClient {
  DiabetAsistanApiClient({
    http.Client? httpClient,
    this.baseUrl = 'http://10.0.2.2:8080',
  }) : _httpClient = httpClient ?? http.Client();

  final http.Client _httpClient;
  final String baseUrl;

  Future<Family> createFamily(String name) async {
    final json = await _post('/api/families', {'name': name});
    return Family.fromJson(json);
  }

  Future<AppUser> createUser({
    required int familyId,
    required String displayName,
    required UserRole role,
    String? email,
  }) async {
    final json = await _post('/api/users', {
      'familyId': familyId,
      'displayName': displayName,
      'role': role.apiValue,
      'email': email,
    });
    return AppUser.fromJson(json);
  }

  Future<List<MealRecord>> getMealsByFamily(int familyId) async {
    final list = await _getList('/api/meals/family/$familyId');
    return list.map(MealRecord.fromJson).toList();
  }

  Future<List<MealRecord>> getPendingMealsByFamily(int familyId) async {
    final list = await _getList('/api/meals/family/$familyId/pending-review');
    return list.map(MealRecord.fromJson).toList();
  }

  Future<MealRecord> createMeal({
    required int familyId,
    required int childId,
    required MealType mealType,
    required String foodName,
    required double estimatedCarbsGram,
    required int createdByUserId,
    String? description,
    String? photoPath,
  }) async {
    final json = await _post('/api/meals', {
      'familyId': familyId,
      'childId': childId,
      'mealTime': DateTime.now().toUtc().toIso8601String(),
      'mealType': mealType.apiValue,
      'foodName': foodName,
      'description': description,
      'photoPath': photoPath,
      'estimatedCarbsGram': estimatedCarbsGram,
      'createdByUserId': createdByUserId,
    });
    return MealRecord.fromJson(json);
  }

  Future<MealRecord> reviewMeal({
    required int mealId,
    required int parentId,
    required ReviewStatus reviewStatus,
    double? correctedCarbsGram,
    String? comment,
  }) async {
    final json = await _post('/api/meals/$mealId/reviews', {
      'parentId': parentId,
      'reviewStatus': reviewStatus.apiValue,
      'correctedCarbsGram': correctedCarbsGram,
      'comment': comment,
    });

    final mealJson = json['meal'];
    if (mealJson is Map<String, dynamic>) {
      return MealRecord.fromJson(mealJson);
    }
    throw const FormatException('Review response did not include a meal object.');
  }

  Future<List<FamilyRecipe>> getFamilyRecipes(int familyId) async {
    final list = await _getList('/api/family-recipes/family/$familyId');
    return list.map(FamilyRecipe.fromJson).toList();
  }

  Future<FamilyRecipe> createFamilyRecipe({
    required int familyId,
    required String name,
    required double totalCarbsGram,
    required double servings,
    required int createdByParentId,
    String? description,
    String? defaultPortionDescription,
  }) async {
    final json = await _post('/api/family-recipes', {
      'familyId': familyId,
      'name': name,
      'description': description,
      'totalCarbsGram': totalCarbsGram,
      'servings': servings,
      'defaultPortionDescription': defaultPortionDescription,
      'createdByParentId': createdByParentId,
    });
    return FamilyRecipe.fromJson(json);
  }

  Future<Map<String, dynamic>> _post(
    String path,
    Map<String, dynamic> payload,
  ) async {
    final response = await _httpClient.post(
      Uri.parse('$baseUrl$path'),
      headers: const {'Content-Type': 'application/json'},
      body: jsonEncode(payload),
    );
    return _decodeObject(response);
  }

  Future<List<Map<String, dynamic>>> _getList(String path) async {
    final response = await _httpClient.get(Uri.parse('$baseUrl$path'));
    final decoded = _decodeAny(response);
    if (decoded is List) {
      return decoded.cast<Map<String, dynamic>>();
    }
    throw FormatException('Expected list response from $path');
  }

  Map<String, dynamic> _decodeObject(http.Response response) {
    final decoded = _decodeAny(response);
    if (decoded is Map<String, dynamic>) {
      return decoded;
    }
    throw const FormatException('Expected object response.');
  }

  Object? _decodeAny(http.Response response) {
    if (response.statusCode < 200 || response.statusCode >= 300) {
      throw ApiException(
        statusCode: response.statusCode,
        message: response.body,
      );
    }
    if (response.body.isEmpty) {
      return null;
    }
    return jsonDecode(response.body);
  }
}

class ApiException implements Exception {
  const ApiException({
    required this.statusCode,
    required this.message,
  });

  final int statusCode;
  final String message;

  @override
  String toString() => 'ApiException($statusCode): $message';
}
