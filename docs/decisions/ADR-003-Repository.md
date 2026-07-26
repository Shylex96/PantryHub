# ADR-003: Use Repository Pattern

## Status

Accepted

## Date

2026-01-01

---

# Context

PantryHub will use multiple data sources in the future:

Current:

```
Room Database
```

Future:

```
Remote API

Synchronization Service

Cloud Storage
```

The application should not depend directly on any specific source.

---

# Decision

PantryHub will use the:

```
Repository Pattern
```

as the abstraction layer between domain and data.

---

# Architecture

```
Presentation

↓

Domain

↓

Repository

↓

Data Sources
```

---

# Reasons

## Separation of Concerns

The UI should not know:

- Where data comes from.
- How it is stored.
- How synchronization works.

---

## Future Synchronization

Without repository:

```
ViewModel

↓

Room
```

Migration would be difficult.

---

With repository:

```
ViewModel

↓

Repository

↓

Room/API
```

---

## Testing

Repositories can be replaced with mocks.

---

# Alternatives Considered

## Direct DAO Access From ViewModel

Rejected.

Problems:

- Tight coupling.
- Hard testing.
- Difficult future changes.

---

## Service Layer Only

Rejected.

Does not solve data source abstraction.

---

# Consequences

Positive:

- Cleaner architecture.
- Easier testing.
- Ready for synchronization.

Negative:

- More initial classes.
- More abstraction.

---

# Future Impact

Every domain operation should interact through repositories.

Examples:

```
ProductRepository

ShoppingListRepository

PurchaseRepository
```

---
Last updated: July 26, 2026
