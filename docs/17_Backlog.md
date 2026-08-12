# PantryHub Backlog

> ⚠️ **Stale status.** The states below (many as `PLANNED`) do NOT reflect reality: much of
> it is already `DONE` (foundation, theme, navigation, product, search, duplicates, lists,
> shopping mode, finish purchase). The **single source of truth for progress is `STATUS.md`**,
> and the order of work is set by `19_Execution_Plan.md`. This backlog is kept only as a
> catalog of stories/acceptance criteria.

## Overview

This document contains the product backlog of PantryHub.

The backlog represents all planned work, improvements and future ideas.

It is a living document that evolves as the product grows.

---

# Backlog Structure

Each item contains:

- ID.
- Type.
- Priority.
- Status.
- Description.
- Acceptance criteria.

---

# Priority Levels

## Critical

Required for the application to work.

---

## High

Important for the first stable version.

---

## Medium

Useful improvements.

---

## Low

Future enhancements.

---

# Status

Possible states:

```
BACKLOG

PLANNED

IN_PROGRESS

BLOCKED

DONE
```

---

# Epic 01 - Project Foundation

## PB-001

### Setup Android Project

Priority:

```
Critical
```

Status:

```
DONE
```

Description:

Create the initial Android project structure.

Includes:

- Kotlin.
- Kotlin DSL.
- Compose.
- Hilt.
- Modular architecture.

Acceptance criteria:

```
Project builds successfully.

Architecture modules exist.

Dependencies are configured.
```

---

## PB-002

### Configure Dependency Management

Priority:

```
Critical
```

Status:

```
PLANNED
```

Description:

Configure:

- Version catalog.
- Gradle conventions.
- Dependency versions.

Acceptance criteria:

```
Dependencies are centralized.

No duplicated versions exist.
```

---

# Epic 02 - Application Core

## PB-010

### Create Application Theme

Priority:

```
High
```

Status:

```
PLANNED
```

Description:

Create the initial design system foundation.

Includes:

- Colors.
- Typography.
- Shapes.
- Material 3 theme.

Acceptance criteria:

```
Light theme works.

Dark theme works.

Components use shared styles.
```

---

## PB-011

### Configure Navigation

Priority:

```
High
```

Status:

```
PLANNED
```

Description:

Create application navigation structure.

Initial destinations:

```
Shopping Lists

Products

Notes

Settings
```

Acceptance criteria:

```
Navigation works.

Screens are isolated.
```

---

# Epic 03 - Product Management

## PB-020

### Create Product Entity

Priority:

```
Critical
```

Status:

```
PLANNED
```

Description:

Implement product domain model.

Includes:

- Name.
- Category.
- Favorite.
- Usage frequency.

Acceptance criteria:

```
Products can be created.

Products can be stored locally.
```

---

## PB-021

### Product Search

Priority:

```
High
```

Status:

```
PLANNED
```

Description:

Implement fast product searching.

Search must support:

- Partial text.
- Alphabetical ordering.
- Frequency ordering.

Example:

Input:

```
pa
```

Results:

```
Pavo

Patatas
```

Acceptance criteria:

```
Results update quickly.

Search ignores accents.
```

---

## PB-022

### Product Duplicate Detection

Priority:

```
High
```

Status:

```
PLANNED
```

Description:

Detect possible duplicated products.

Cases:

Automatic:

```
Arroz

arroz
```

Review:

```
Lenteja

Lentejas
```

Different:

```
Cava

Caña
```

Acceptance criteria:

```
Duplicate warnings are generated.

User decides ambiguous cases.
```

---

# Epic 04 - Shopping Lists

## PB-030

### Create Shopping List

Priority:

```
Critical
```

Status:

```
PLANNED
```

Description:

Users can create shopping lists.

Sprint 1 (feature-shopping):
- Create `ShoppingListsScreen`.
- Implement `ShoppingViewModel` with memory data.
- UI components: `ShoppingListCard`.

Acceptance criteria:

```
List is stored.
List appears in main screen.
```

---

## PB-031

### Add Products To List

Priority:

```
Critical
```

Status:

```
PLANNED
```

Description:

Allow adding products quickly.

Methods:

- Search.
- Categories.
- Favorites.

Acceptance criteria:

```
Product appears in list.

Duplicate items are controlled.
```

---

## PB-032

### Duplicate Existing List

Priority:

```
High
```

Status:

```
PLANNED
```

Description:

Create a new list based on an existing one.

Purpose:

Avoid rebuilding similar shopping lists.

Example:

```
Christmas 2025

↓

Christmas 2026
```

Acceptance criteria:

```
New independent list created.

Products are reused.
```

---

# Epic 05 - Shopping Mode

## PB-040

### Start Shopping Mode

Priority:

```
Critical
```

Status:

```
PLANNED
```

Description:

Allow users to actively buy items.

Behaviour:

Pending items remain above.

Completed items move below.

Acceptance criteria:

```
Items can be marked completed.

Completed items are visually separated.
```

---

## PB-041

### Finish Shopping Session

Priority:

```
High
```

Status:

```
PLANNED
```

Description:

Complete shopping.

Options:

```
Finish

Finish with purchase information
```

Acceptance criteria:

```
Purchase history can be created.
```

---

# Epic 06 - Purchase History

## PB-050

### Register Purchase

Priority:

```
Medium
```

Status:

```
PLANNED
```

Description:

Store:

- Date.
- Amount.
- Supermarket.

Acceptance criteria:

```
Purchase appears in history.
```

---

## PB-051

### Purchase Reports Foundation

Priority:

```
Low
```

Status:

```
BACKLOG
```

Description:

Prepare future analytics.

Examples:

- Spending.
- Supermarket comparison.
- Product frequency.

---

# Epic 07 - Notes

## PB-060

### Create Notes System

Priority:

```
Medium
```

Status:

```
PLANNED
```

Description:

Allow users to create notes.

Examples:

```
Tomatoes cheaper on Fridays.

Remember batteries.
```

Acceptance criteria:

```
Notes can be created and edited.
```

---

# Epic 08 - Import Export

## PB-070

### Export JSON Backup

Priority:

```
High
```

Status:

```
PLANNED
```

Acceptance criteria:

```
Complete JSON generated.

Schema version included.
```

---

## PB-071

### Import JSON Backup

Priority:

```
High
```

Status:

```
PLANNED
```

Acceptance criteria:

```
Data restored correctly.

Conflicts detected.
```

---

# Epic 09 - Localization

## PB-080

### Multi-language Support

Priority:

```
High
```

Status:

```
PLANNED
```

Languages:

Initial:

```
Spanish

English
```

Acceptance criteria:

```
All texts use resources.

Language can change easily.
```

---

# Epic 10 - Household Synchronization

## PB-100

### Create Household Model

Priority:

```
Low
```

Status:

```
BACKLOG
```

Description:

Prepare shared environments.

---

## PB-101

### QR Household Invitation

Priority:

```
Low
```

Status:

```
BACKLOG
```

Description:

Allow joining households through QR.

---

## PB-102

### Cloud Synchronization

Priority:

```
Low
```

Status:

```
BACKLOG
```

Description:

Synchronize:

- Lists.
- Products.
- Purchases.

---

# Epic 11 - Quality

## PB-110

### Automated Testing

Priority:

```
High
```

Status:

```
PLANNED
```

Includes:

- Unit tests.
- Database tests.
- UI tests.

---

# Version 1.0 Scope

The following features define the first stable release:

```
Product management

Categories

Shopping lists

Product search

Favorites

Shopping mode

Notes

Import/export

Dark mode

Localization

Basic purchase history
```

---

# Post 1.0 Ideas

Future possibilities:

```
Household synchronization

QR invitations

Advanced reports

Price tracking

Supermarket statistics

Smart suggestions

AI assisted shopping
```

---

# Backlog Maintenance Rules

The backlog should be updated when:

- A feature is planned.
- A feature changes scope.
- A requirement is removed.
- A release is completed.

---

# Final Goal

The PantryHub backlog exists to transform ideas into controlled implementation steps while keeping the product vision aligned.

---
Last updated: July 26, 2026
