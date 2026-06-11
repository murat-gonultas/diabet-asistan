enum UserRole {
  child('CHILD'),
  parent('PARENT');

  const UserRole(this.apiValue);
  final String apiValue;

  static UserRole fromApi(String value) {
    return UserRole.values.firstWhere(
      (role) => role.apiValue == value,
      orElse: () => UserRole.child,
    );
  }
}

enum MealType {
  breakfast('BREAKFAST'),
  lunch('LUNCH'),
  dinner('DINNER'),
  snack('SNACK'),
  other('OTHER');

  const MealType(this.apiValue);
  final String apiValue;
}

enum MealStatus {
  draft('DRAFT'),
  pendingParentReview('PENDING_PARENT_REVIEW'),
  approved('APPROVED'),
  corrected('CORRECTED');

  const MealStatus(this.apiValue);
  final String apiValue;

  static MealStatus fromApi(String value) {
    return MealStatus.values.firstWhere(
      (status) => status.apiValue == value,
      orElse: () => MealStatus.pendingParentReview,
    );
  }
}

enum ReviewStatus {
  approved('APPROVED'),
  corrected('CORRECTED');

  const ReviewStatus(this.apiValue);
  final String apiValue;
}

class Family {
  const Family({
    required this.id,
    required this.name,
  });

  final int id;
  final String name;

  factory Family.fromJson(Map<String, dynamic> json) {
    return Family(
      id: json['id'] as int,
      name: json['name'] as String,
    );
  }
}

class AppUser {
  const AppUser({
    required this.id,
    required this.familyId,
    required this.displayName,
    required this.role,
    required this.active,
    this.email,
  });

  final int id;
  final int familyId;
  final String displayName;
  final UserRole role;
  final String? email;
  final bool active;

  factory AppUser.fromJson(Map<String, dynamic> json) {
    return AppUser(
      id: json['id'] as int,
      familyId: json['familyId'] as int,
      displayName: json['displayName'] as String,
      role: UserRole.fromApi(json['role'] as String),
      email: json['email'] as String?,
      active: json['active'] as bool? ?? true,
    );
  }
}

class MealRecord {
  const MealRecord({
    required this.id,
    required this.familyId,
    required this.childId,
    required this.mealTime,
    required this.mealType,
    required this.foodName,
    required this.estimatedCarbsGram,
    required this.status,
    this.description,
    this.photoPath,
    this.finalCarbsGram,
    this.createdByUserId,
    this.reviewedByUserId,
  });

  final int id;
  final int familyId;
  final int childId;
  final DateTime mealTime;
  final MealType mealType;
  final String foodName;
  final String? description;
  final String? photoPath;
  final double estimatedCarbsGram;
  final double? finalCarbsGram;
  final MealStatus status;
  final int? createdByUserId;
  final int? reviewedByUserId;

  factory MealRecord.fromJson(Map<String, dynamic> json) {
    return MealRecord(
      id: json['id'] as int,
      familyId: json['familyId'] as int,
      childId: json['childId'] as int,
      mealTime: DateTime.parse(json['mealTime'] as String),
      mealType: MealType.values.firstWhere(
        (type) => type.apiValue == json['mealType'],
        orElse: () => MealType.other,
      ),
      foodName: json['foodName'] as String,
      description: json['description'] as String?,
      photoPath: json['photoPath'] as String?,
      estimatedCarbsGram: _toDouble(json['estimatedCarbsGram']),
      finalCarbsGram: json['finalCarbsGram'] == null
          ? null
          : _toDouble(json['finalCarbsGram']),
      status: MealStatus.fromApi(json['status'] as String),
      createdByUserId: json['createdByUserId'] as int?,
      reviewedByUserId: json['reviewedByUserId'] as int?,
    );
  }

  double get displayCarbsGram => finalCarbsGram ?? estimatedCarbsGram;
}

class FamilyRecipe {
  const FamilyRecipe({
    required this.id,
    required this.familyId,
    required this.name,
    required this.totalCarbsGram,
    required this.servings,
    required this.carbsPerServing,
    required this.active,
    this.description,
    this.defaultPortionDescription,
    this.createdByParentId,
  });

  final int id;
  final int familyId;
  final String name;
  final String? description;
  final double totalCarbsGram;
  final double servings;
  final double carbsPerServing;
  final String? defaultPortionDescription;
  final int? createdByParentId;
  final bool active;

  factory FamilyRecipe.fromJson(Map<String, dynamic> json) {
    return FamilyRecipe(
      id: json['id'] as int,
      familyId: json['familyId'] as int,
      name: json['name'] as String,
      description: json['description'] as String?,
      totalCarbsGram: _toDouble(json['totalCarbsGram']),
      servings: _toDouble(json['servings']),
      carbsPerServing: _toDouble(json['carbsPerServing']),
      defaultPortionDescription: json['defaultPortionDescription'] as String?,
      createdByParentId: json['createdByParentId'] as int?,
      active: json['active'] as bool? ?? true,
    );
  }
}

double _toDouble(Object? value) {
  if (value is int) {
    return value.toDouble();
  }
  if (value is double) {
    return value;
  }
  if (value is String) {
    return double.parse(value);
  }
  throw FormatException('Unsupported number value: $value');
}
