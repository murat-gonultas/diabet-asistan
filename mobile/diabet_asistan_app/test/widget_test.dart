import 'package:diabet_asistan_app/main.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('app shows safety-first bootstrap screen', (tester) async {
    await tester.pumpWidget(const DiabetAsistanApp());

    expect(find.text('Diabet Asistan MVP'), findsOneWidget);
    expect(find.text('Safety-first carbohydrate logging'), findsOneWidget);
    expect(find.textContaining('No insulin dose calculation'), findsOneWidget);
    expect(find.byIcon(Icons.health_and_safety), findsOneWidget);
  });
}
