# PantryHub Architecture

## Overview

This document defines the software architecture of PantryHub.

The architecture is designed to support:

- Long-term maintainability.
- Feature scalability.
- Testability.
- Offline-first development.
- Future synchronization.
- Household collaboration.

The architecture follows:

- Clean Architecture principles.
- MVVM pattern.
- Repository pattern.
- Unidirectional data flow.

---

# Architecture Goals

The architecture should allow PantryHub to evolve from:

```
Personal shopping application
```

into:

```
Household purchasing platform
```

without requiring a complete rewrite.

---

# Architectural Principles

## Separation of Responsibilities

Each layer has a clear responsibility.

A component should only know what it needs to know.

---

## Dependency Direction

Dependencies should always point inward.

Allowed:

```
Presentation

↓

Domain

↓

Data
```

Not allowed:

```
Domain

↓

Presentation
```

The business logic must never depend on UI.

---

## Testability

Business logic should be testable without:

- Android framework.
- Database.
- Network.
- UI.

---

# High Level Architecture

```
                 App Module

                    |

              Presentation Layer

                    |

              Domain Layer

                    |

               Data Layer

                    |

        ------------------------

        Local       Remote (future, 1.1+)

        Room        Retrofit
```

---

# Module Structure

PantryHub uses a modular architecture: `app`, seven `core-*` modules and five `feature-*` modules (the authoritative list is `settings.gradle.kts`).

```
PantryHub

├── app
├── core-model
├── core-common
├── core-database
├── core-data
├── core-domain
├── core-designsystem
├── core-navigation
├── feature-shopping
├── feature-products
├── feature-notes
├── feature-settings
├── feature-importexport
└── docs
```

There are no modules named `core`, `data`, `domain`, `feature-lists` or `feature-qr`. The Clean Architecture *layers* (presentation / domain / data) are realized by these modules as described below.

---

# Module Responsibilities

# App Module

## Purpose

Application entry point.

Contains:

- Application class (Hilt root).
- MainActivity.
- Navigation host and bottom navigation (Lists / Products / Notes / Settings).
- Global configuration.

---

## Dependencies

`app` depends on everything: all `core-*` modules and all `feature-*` modules.

---

# Core Modules

The `core-*` modules hold everything shared by more than one feature. Each has a single responsibility:

| Module | Responsibility |
|---|---|
| `core-model` | Domain models as pure Kotlin data classes (Product, Category, ShoppingList, ShoppingListItem, Purchase, Note, AppSettings). No Android dependencies. |
| `core-common` | Shared utilities: result / error types, dispatchers, normalization and date helpers, extensions. |
| `core-database` | Room database (`PantryHubDatabase`, version 4), entities, DAOs, migrations and the database Hilt module. |
| `core-data` | Repository implementations (`Offline*`), entity ↔ model mappers, DataStore preferences and the data Hilt bindings. |
| `core-domain` | Repository interfaces and use cases (product, shopping, notes, backup). Depends only on `core-model` and `core-common`. |
| `core-designsystem` | Material 3 theme (colors, typography, shapes, motion) and reusable `Pantry*` Compose components. |
| `core-navigation` | Type-safe route definitions shared by the features and the app navigation host. |

## Rules

- Core modules must remain lightweight and focused; business logic lives in `core-domain`, not in `core-common` or `core-designsystem`.
- `core-*` modules may depend only on lower-level `core-*` modules (e.g. `core-data` → `core-database`, `core-domain`, `core-model`; `core-designsystem` → `core-model` at most). They never depend on features or `app`.

---

# Domain Layer (`core-domain` + `core-model`)

## Purpose

Contains the business rules of PantryHub.

This is the most stable layer.

---

## Contains

### Entities (`core-model`)

```
Product
Category
ShoppingList
ShoppingListItem
Purchase
Note
AppSettings
```

---

### Use Cases (`core-domain`)

Examples:

```
CreateShoppingList

AddProductToList

CompleteShoppingItem

CloneShoppingList

ExportData

ImportData
```

---

### Repository Interfaces (`core-domain`)

Example:

```
ProductRepository

CategoryRepository

ShoppingListRepository

PurchaseRepository

NoteRepository

BackupRepository

SettingsRepository
```

---

## Dependencies

The domain layer depends on:

```
core-model, core-common
```

and pure Kotlin libraries only (coroutines, kotlinx-datetime). No Android framework, Room or Compose.

---

# Data Layer (`core-data` + `core-database`)

## Purpose

Provides data access implementations.

---

## Contains

- Room database, entities and DAOs (`core-database`).
- Repository implementations bound to the `core-domain` interfaces via Hilt (`core-data`).
- Entity ↔ model mappers (`core-data`).
- DataStore-backed preferences (`core-data`).

A remote data source (Retrofit) is **not** present; it is planned for the connected phases (1.1+, see `12_Synchronization.md`). The repository interfaces are the seam where synced implementations will be plugged in.

---

## Responsibilities

Examples:

```
Database entity

↓

Domain model
```

Conversion happens here.

---

# Feature Modules

## Purpose

Contains user-facing functionality. Current feature modules:

```
feature-shopping       — shopping lists, list detail, shopping mode

feature-products       — product catalog and categories

feature-notes          — notes

feature-settings       — settings and help

feature-importexport   — JSON import / export
```

---

Each feature contains presentation code only (screens, ViewModels, UI state, intents and feature-local components):

```
feature-name

├── ui / screens
├── viewmodel
└── components
```

Business rules stay in `core-domain`; features call use cases and never touch DAOs directly.

---

# MVVM Architecture

Each screen follows:

```
Composable

↓

ViewModel

↓

Use Case

↓

Repository

↓

Data Source
```

---

# Presentation Layer

## Responsibilities

The presentation layer handles:

- UI rendering.
- User actions.
- UI state.
- Navigation events.

---

## Should Not Contain

Avoid:

- Database calls.
- Business rules.
- Complex calculations.

---

# ViewModel

## Responsibilities

The ViewModel:

- Receives user events.
- Executes use cases.
- Exposes UI state.
- Handles screen logic.

---

Example:

```
User clicks Add Product

↓

ViewModel

↓

AddProductUseCase

↓

Repository

↓

Database
```

---

# UI State

Each screen should expose a single state object.

Example:

```
ShoppingListUiState
```

Contains:

```
items

isLoading

errorMessage

selectedFilters
```

---

## State Flow

Recommended:

```
StateFlow
```

Example:

```
ViewModel

↓

StateFlow

↓

Compose
```

---

# User Events

User actions should be represented explicitly.

Example:

```
ShoppingListEvent

AddProduct

RemoveProduct

CompleteItem

OpenShoppingMode
```

---

# Unidirectional Data Flow

The application follows:

```
User Action

↓

Event

↓

ViewModel

↓

New State

↓

UI Update
```

---

# Repository Pattern

Repositories abstract data sources.

Example:

```
ProductRepository
```

The UI does not know whether data comes from:

- Room.
- API.
- Cache.

---

# Repository Responsibilities

Repositories handle:

- Data retrieval.
- Data synchronization.
- Cache strategy.
- Data mapping.

---

# Database Architecture

Room is the first data source.

Example:

```
Room Database

↓

DAO

↓

Repository

↓

Use Case

↓

ViewModel
```

---

# Future Synchronization Architecture

The architecture prepares for:

```
Local Database

        +

Remote Database

        +

Synchronization Engine
```

---

Future flow:

```
User Action

↓

Local Update

↓

Sync Queue

↓

Remote Service

↓

Other Devices
```

---

# Error Handling

Errors should be represented consistently.

Recommended approach:

```
Result<T>
```

or sealed classes.

Example:

```
Success

Error

Loading
```

---

# Loading States

Loading should be explicit.

Example:

```
UiState

isLoading = true
```

Avoid hidden loading behavior.

---

# Navigation Architecture

Navigation should be managed centrally.

Example:

```
Navigation Graph

├── Lists

├── Products

├── Shopping Mode

├── Notes

└── Settings
```

---

# Feature Independence

Features should be isolated.

Example:

```
feature-products
```

should not directly access:

```
feature-shopping
```

Communication should happen through:

- Domain layer (`core-domain` use cases and repositories).
- Shared contracts (`core-model`, `core-navigation` routes).

---

# Dependency Rules

Allowed:

```
app
 |
 feature-*          (features depend on core-* only; never on each other)
 |
 core-domain  core-designsystem  core-navigation
 |
 core-data → core-database
 |
 core-model  core-common
```

In words: features depend on `core-*`; features never depend on each other; `app` depends on everything.

---

Forbidden:

```
feature-products

directly importing

feature-shopping
```

---

# Offline First Strategy

Local data is the source of truth.

Priority:

```
Local database

↓

UI

↓

Remote synchronization
```

---

# Future Sync Preparation

Entities should consider:

- Unique identifiers.
- Creation dates.
- Modification dates.
- Synchronization status.

---

# Security Considerations

Sensitive information should:

- Stay local by default.
- Be exported intentionally.
- Be synchronized only with permission.

---

# Testing Strategy

Each layer has different tests.

---

## Domain Tests

Test:

- Use cases.
- Business rules.
- Validations.

---

## Data Tests

Test:

- Database operations.
- Repository behaviour.

---

## Presentation Tests

Test:

- ViewModel states.
- User events.

---

## UI Tests

Test:

- User journeys.
- Critical screens.

---

# Architecture Decision Rules

Before adding a dependency or creating a new module ask:

1. Does it improve maintainability?
2. Does it respect layer boundaries?
3. Can it be tested?
4. Does it prepare for future growth?

---

# Final Architecture Goal

PantryHub architecture should allow the project to grow from:

```
A simple local shopping list
```

to:

```
A scalable household shopping ecosystem
```

while keeping the codebase understandable and maintainable.

---
Last updated: September 11, 2026
