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

        Local       Remote

        Room        Retrofit
```

---

# Module Structure

PantryHub uses a modular architecture.

```
PantryHub

├── app
├── core
├── domain
├── data
├── feature
└── docs
```

---

# Module Responsibilities

# App Module

## Purpose

Application entry point.

Contains:

- Application class.
- MainActivity.
- Navigation setup.
- Global configuration.

---

## Dependencies

Can depend on:

```
core
domain
feature
data
```

---

# Core Module

## Purpose

Shared utilities used across modules.

Contains:

- Common extensions.
- Constants.
- Shared utilities.
- Result wrappers.
- Date utilities.

---

## Rules

Core must remain lightweight.

Avoid placing business logic here.

---

# Domain Module

## Purpose

Contains the business rules of PantryHub.

This is the most stable layer.

---

## Contains

### Entities

Examples:

```
Product
Category
ShoppingList
ShoppingItem
Purchase
Note
```

---

### Use Cases

Examples:

```
CreateShoppingList

AddProductToList

CompleteShoppingItem

ExportData

ImportData
```

---

### Repository Interfaces

Example:

```
ProductRepository

ShoppingListRepository

PurchaseRepository
```

---

## Dependencies

Domain should depend on:

```
Nothing
```

or only pure Kotlin libraries.

---

# Data Module

## Purpose

Provides data access implementations.

---

## Contains

- Room database.
- DAO interfaces.
- Retrofit services.
- Repository implementations.
- Data mappers.

---

## Responsibilities

Examples:

```
Database entity

↓

Domain entity
```

Conversion happens here.

---

# Feature Modules

## Purpose

Contains user-facing functionality.

Example:

```
feature-shopping

feature-products

feature-lists

feature-settings
```

---

Each feature contains:

```
feature-name

├── presentation
├── domain
└── components
```

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

- Domain layer.
- Shared contracts.

---

# Dependency Rules

Allowed:

```
app
 |
 feature
 |
 domain
 |
 data
 |
 core
```

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
Last updated: July 26, 2026
