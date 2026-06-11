# diabet-asistan
Safety-first carbohydrate logging assistant for children with Type 1 diabetes

# Diabet Asistan

Diabet Asistan is a safety-first carbohydrate logging assistant for children with Type 1 diabetes and their parents.

## Core Safety Boundaries

Diabet Asistan does **not** calculate insulin doses.  
Diabet Asistan does **not** control insulin pumps.  
Diabet Asistan does **not** provide medical treatment recommendations.  

The application only supports meal logging, estimated carbohydrate tracking, parent review, family recipes, and future AI-assisted carbohydrate estimation.

## Initial MVP Scope

- Child and parent roles
- Meal logging
- Meal photo reference
- Manual estimated carbohydrate entry
- Parent review and correction
- Family recipes
- Meal history

## Out of Scope for MVP v0.1

- Insulin dose calculation
- Pump control
- Automatic bolus
- Medical treatment recommendations
- Dexcom integration
- AI-based photo recognition

## Planned Stack

- Flutter mobile app
- Spring Boot backend
- PostgreSQL or SQLite depending on phase
- Later AI adapter service
- Later Dexcom OAuth integration

## Repository Structure

```text
backend/
mobile/
docs/
  product/
  safety/
  architecture/
  kanban/