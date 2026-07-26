# ADR-008: Use Clean Architecture Principles

## Status

Accepted

## Date

2026-01-01

---

# Context

PantryHub contains important business logic:

- Product matching.
- Duplicate detection.
- Shopping workflow.
- Purchase history.
- Import/export.
- Synchronization.

Business rules should not depend on Android frameworks.

---

# Decision

PantryHub will follow Clean Architecture principles.

Main layers:

```
Presentation

Domain

Data
```

---

# Layer Responsibilities

## Presentation

Contains:

- Compose screens.
- ViewModels.
- UI state.

Responsible for:

```
Displaying information

Handling user interaction
```

---

## Domain

Contains:

- Entities.
- Use Cases.
- Business rules.

Responsible for:

```
What the application does
```

---

## Data

Contains:

- Room.
- Retrofit.
- Repositories implementation.
- Mappers.

Responsible for:

```
Where data comes from
```

---

# Dependency Direction

Dependencies flow inward:

```
Presentation

↓

Domain

↓

Data
```

The domain layer should not depend on Android.

---

# Reasons

## Business Logic Protection

Rules like duplicate detection should survive UI changes.

---

## Testability

Domain logic can be tested without Android dependencies.

---

## Future Evolution

Allows:

```
Room

↓

Remote API

↓

Different storage
```

without rewriting business rules.

---

# Alternatives Considered

## Simple MVVM Only

Rejected.

Reason:

Not enough separation as the application grows.

---

# Consequences

Positive:

- Maintainable architecture.
- Easier testing.
- Clear responsibilities.

Negative:

- More classes.
- More initial structure.

---

# Future Impact

New features should follow:

```
Feature

├── presentation

├── domain

└── data
```

when complexity requires it.

---
Last updated: July 26, 2026
