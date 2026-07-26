# PantryHub Roadmap

## Overview

This document defines the evolution plan of PantryHub.

The roadmap is organized into phases that progressively increase the application's capabilities while maintaining a stable architecture.

---

# Development Philosophy

PantryHub will follow these principles during development:

- Build a solid offline-first foundation.
- Prioritize daily usefulness over feature quantity.
- Avoid premature complexity.
- Introduce collaboration only after the personal experience is mature.
- Preserve user data compatibility between versions.

---

# Phase 0 - Foundation (v0.1.0-foundation)

## Objective
Create the technical and product foundation required for long-term development.

## Status
**DONE** (July 26, 2026)

## Scope
- Initial architecture foundation.
- Modular structure established (`core-*`, `feature-*`).
- Dependency injection with Hilt & KSP configured.
- Base documentation and ADRs.
- Jetpack Compose & Material 3 integration.
- Type-safe navigation.

---

# Phase 1 - Personal Shopping Assistant (MVP)

## Objective
Create a complete offline shopping management application for individual users.

## Current Focus
**Fase 7: feature-shopping**

### Sprint 1: UI & Navigation Foundation
- Create Shopping Lists screen.
- Set up feature-specific navigation.
- Implement ViewModel with in-memory fake data.
- Build reusable Compose components.
- Implement visual "Shopping Mode" logic.

## Scope

### Product Management
- Create, edit, and delete products.
- Assign categories and mark favorites.
- Products as reusable entities.

### Categories
- Browse and manage product categories.

### Shopping Lists
- Create, rename, delete, and duplicate lists.
- Strict hierarchy: `ShoppingList` -> `ShoppingListItem` -> `Product`.

### Shopping Mode
- Optimized UI for supermarket usage.
- Items reordering: completed items move below pending.

### Purchase Completion
- Save purchase date, price, and supermarket.

### Search System
- Fast search by name and category.

### Import and Export
- JSON backup and restore.

---

# Phase 2 - Personalization
## Objective
Make PantryHub faster and more intelligent based on user habits.
- Favorites and purchase history insights.
- Smart suggestions.
- Advanced product matching (duplicate detection).

---

# Phase 3 - Household Collaboration
## Objective
Allow multiple users to share shopping information.
- Shared workspaces and cloud synchronization.
- Real-time updates and conflict resolution.
- QR invitations.

---

# Phase 4 - Analytics and Insights
## Objective
Transform purchase data into useful information.
- Spending reports and purchase analysis.

---

# Phase 5 - Advanced Features
## Objective
Introduce intelligent automation.
- Automatic list generation and consumption prediction.

---

# Milestones Summary
- **v0.1.0-foundation**: Initial architecture (Completed).
- **v0.2.0-domain**: Complete Domain Model and business rules.
- **v0.3.0-storage**: Real persistence with Room and Repositories.
- **v0.4.0-shopping-flow**: Functional shopping flows.
- **v1.0.0-stable**: Final polished application.

---
Last updated: July 26, 2026
