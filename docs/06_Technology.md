# PantryHub Technology Stack

## Overview

This document defines the technology stack used by PantryHub.

The objective is to use modern Android development practices while maintaining:

- Scalability.
- Maintainability.
- Testability.
- Long-term compatibility.

Technology decisions should support both the current offline application and future capabilities such as synchronization and household collaboration.

---

# Platform

## Android

PantryHub is an Android-first application.

Initial target:

```
Android Application
```

Future possibilities:

- Tablet optimization.
- Multi-device support.
- Wearable integrations if relevant.

---

# Minimum Android Version

## Minimum SDK

Current value:

```
minSdk = 28
```

(Android 9.0 Pie)

---

## Reason

Android 9.0 provides:

- Strong modern API support.
- Better background execution behavior.
- Good current device compatibility.
- Reduced need for compatibility workarounds.

---

## Target SDK

The application should always target the latest stable Android SDK available.

Current values:

```
targetSdk = 35
compileSdk = 35
```

Toolchain: Kotlin 2.0.21, Java 21 (`sourceCompatibility` / `targetCompatibility` = 21).

---

# Programming Language

## Kotlin

Kotlin is the primary development language.

Reasons:

- Official Android language.
- Null safety.
- Coroutines support.
- Modern syntax.
- Excellent Compose integration.

---

## Kotlin Features

The project should use:

- Coroutines.
- Flow.
- Extension functions.
- Sealed classes.
- Data classes.
- Immutable models where possible.

---

# Build System

## Gradle Kotlin DSL

The project uses:

```
build.gradle.kts
```

instead of Groovy.

---

## Reasons

Benefits:

- Type safety.
- Better IDE support.
- Kotlin consistency.
- Easier maintenance.

---

# Version Management

## Version Catalog

Dependencies are managed through:

```
gradle/libs.versions.toml
```

---

## Reasons

Provides:

- Central dependency management.
- Consistent versions.
- Easier upgrades.
- Cleaner Gradle files.

---

# UI Framework

## Jetpack Compose

PantryHub uses:

```
Jetpack Compose
```

for UI development.

---

## Reasons

Compose provides:

- Declarative UI.
- Faster development.
- Better state management.
- Modern Android support.
- Easier component reuse.

---

## UI Architecture

The UI layer follows:

```
State

↓

Composable

↓

User Event

↓

ViewModel
```

---

# Design System

UI components are based on:

```
Material 3
```

Including:

- Theme system.
- Components.
- Accessibility.
- Dynamic colors.

---

# Architecture Pattern

## MVVM

PantryHub follows:

```
Model
View
ViewModel
```

---

## Responsibilities

### View

Responsible for:

- Rendering UI.
- Sending user actions.

---

### ViewModel

Responsible for:

- UI state.
- Business interaction coordination.
- Calling use cases.

---

### Model

Contains:

- Domain entities.
- Data structures.

---

# Clean Architecture

The project follows a layered architecture.

Main layers:

```
Presentation

Domain

Data
```

---

## Domain Layer

Contains:

- Business rules.
- Use cases.
- Entities.

Should not depend on:

- Android framework.
- Room.
- Retrofit.
- Compose.

---

## Data Layer

Contains:

- Database.
- Network.
- Repositories implementations.
- Data sources.

---

## Presentation Layer

Contains:

- Screens.
- ViewModels.
- UI state.
- User interactions.

---

# Dependency Injection

## Hilt

PantryHub uses:

```
Dagger Hilt
```

---

## Reasons

Hilt provides:

- Android integration.
- Dependency lifecycle management.
- Test support.
- Less boilerplate.

---

# Local Database

## Room

Local persistence uses:

```
Room Database
```

---

## Reasons

Room provides:

- SQLite abstraction.
- Compile-time validation.
- Migration support.
- Coroutine support.

---

## Usage

Room will store:

- Products.
- Categories.
- Shopping lists.
- Shopping items.
- Notes.
- Purchase history.

---

# Asynchronous Programming

## Kotlin Coroutines

Used for:

- Background work.
- Database operations.
- Network operations.

---

## Flow

Used for:

- Reactive data streams.
- Database observation.
- UI updates.

Example:

```
Room

↓

Flow

↓

ViewModel

↓

Compose UI
```

---

# Network Layer

## Retrofit

Used for future network communication.

Current MVP:

```
Offline first
```

Future:

- Synchronization.
- User accounts.
- Cloud services.

---

## HTTP Client

Recommended:

```
OkHttp
```

with:

- Logging interceptor.
- Authentication support.
- Connection management.

---

# Serialization

## Kotlin Serialization

Preferred:

```
kotlinx.serialization
```

---

## Usage

Used for:

- JSON import/export.
- Future API communication.
- Data migration formats.

---

# Preferences

## DataStore

Used instead of SharedPreferences.

---

## Usage

Stores:

- User preferences.
- Theme selection.
- Language.
- Application settings.

---

# Navigation

## Navigation Compose

Used for application navigation.

---

## Reasons

Provides:

- Compose integration.
- Type-safe navigation possibilities.
- State handling.

---

# Image Loading

Recommended:

```
Coil
```

---

## Usage

Future purposes:

- Product images.
- User avatars.
- Household profiles.

---

# Testing Stack

## Unit Testing

Framework:

```
JUnit
```

Used for:

- Use cases.
- Business rules.
- ViewModels.

---

## Flow Testing

Recommended:

```
Turbine
```

Used for testing Kotlin Flow emissions.

---

## UI Testing

Compose testing framework:

```
Compose UI Test
```

Used for:

- User interactions.
- Screens.
- Components.

---

# Code Quality

## Static Analysis

Recommended:

```
Detekt
```

Purpose:

- Detect code smells.
- Maintain consistency.

---

## Formatting

Recommended:

```
KtLint
```

Purpose:

- Kotlin formatting.
- Code consistency.

---

# Modular Architecture

The project uses multiple Gradle modules: `app`, seven `core-*` modules and five `feature-*` modules (see `settings.gradle.kts`).

Structure:

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

Dependency direction:

- Feature modules depend on `core-*` modules only.
- Feature modules never depend on each other.
- `core-*` modules depend only on lower-level `core-*` modules (e.g. `core-data` → `core-database`, `core-domain`, `core-model`).
- `app` depends on everything and wires the navigation graph and Hilt root.

There are no modules named `core`, `data`, `domain`, `feature-lists` or `feature-qr`. A QR / sharing feature module may be added in 1.2 (see `01_Roadmap.md`).

---

# Module Responsibilities

## app

Application entry point.

Contains:

- MainActivity.
- Application class (Hilt root).
- Navigation host and bottom navigation (Lists / Products / Notes / Settings).

---

## core-model

Domain models as pure Kotlin data classes (products, categories, shopping lists and items, notes). No Android dependencies.

---

## core-common

Shared Kotlin utilities: result / error types, dispatchers, extensions and constants.

---

## core-database

Room database (`PantryHubDatabase`, version 4), entities, DAOs, migrations and the database Hilt module.

---

## core-data

Repository implementations (offline), entity ↔ model mappers, DataStore-backed preferences and the data Hilt bindings.

---

## core-domain

Repository interfaces and use cases (product, shopping, notes, backup). Depends only on `core-model` and `core-common`.

---

## core-designsystem

Theme (colors, typography, shapes, motion) and reusable Compose components (`Pantry*`).

---

## core-navigation

Type-safe route definitions shared by the features and the app navigation host.

---

## feature-shopping

Shopping lists, list detail and shopping mode.

---

## feature-products

Product catalog (CRUD, search, favorites, duplicates) and categories.

---

## feature-notes

Notes list and editor.

---

## feature-settings

Settings (theme, dynamic color, language) and help.

---

## feature-importexport

JSON export and import with preview and conflict handling.

---

# Development Principles

Technology choices must follow these rules:

- Prefer official Android solutions.
- Avoid unnecessary dependencies.
- Keep modules independent.
- Favor maintainability over shortcuts.
- Prepare for future growth.
- Document important decisions.

---

# Final Technology Goal

The technology stack should allow PantryHub to evolve from:

```
Offline shopping application
```

into:

```
A scalable household purchasing platform
```

without requiring a complete rewrite.

---
Last updated: September 11, 2026
