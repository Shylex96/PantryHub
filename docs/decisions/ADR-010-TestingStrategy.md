# ADR-010: Automated Testing Strategy

## Status

Accepted

## Date

2026-01-01

---

# Context

PantryHub contains complex business behavior:

- Product similarity detection.
- List cloning.
- Import/export.
- Shopping workflow.
- Future synchronization.

Manual testing alone is not enough.

---

# Decision

PantryHub will implement automated testing following the testing pyramid.

---

# Testing Layers

```
Unit Tests

↓

Integration Tests

↓

UI Tests
```

---

# Unit Tests

Highest priority.

Used for:

- Use Cases.
- Domain logic.
- Validators.
- Mappers.

---

# Integration Tests

Used for:

- Room database.
- Repository behaviour.
- Data flows.

---

# UI Tests

Used for:

- Critical user journeys.
- Navigation.
- Shopping flows.

---

# Reasons

## Prevent Regression

Existing functionality should remain stable.

---

## Safe Refactoring

Architecture can evolve without fear.

---

## Quality Control

Important business rules remain protected.

---

# Testing Tools

Recommended:

```
JUnit

MockK

Turbine

Compose UI Test

Room Testing
```

---

# Alternatives Considered

## Manual Testing Only

Rejected.

Reason:

Insufficient for long-term growth.

---

# Consequences

Positive:

- Higher confidence.
- Safer releases.
- Better maintainability.

Negative:

- Initial development time increases.

---

# Future Impact

Every new feature should include:

```
Implementation

+

Tests

+

Documentation update
```

---
Last updated: July 26, 2026
