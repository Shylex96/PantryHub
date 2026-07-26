# ADR-004: Modular Application Architecture

## Status

Accepted

## Date

2026-01-01

---

# Context

PantryHub is expected to grow.

Future features:

- Household synchronization.
- Reports.
- QR system.
- Advanced analytics.

A single application module would become difficult to maintain.

---

# Decision

PantryHub will use a modular architecture.

Initial modules:

```
app

core

feature
```

Future:

```
core:database

core:network

core:designsystem

feature:shopping

feature:products

feature:settings
```

---

# Reasons

## Scalability

Each feature can evolve independently.

---

## Better Separation

Example:

Product feature should not know:

- Database implementation.
- Network implementation.

---

## Faster Development

Smaller modules:

- Compile faster.
- Easier navigation.
- Clear ownership.

---

# Alternatives Considered

## Single Module

Rejected.

Reason:

Will become difficult with application growth.

---

## Full Modularization From Day One

Rejected.

Reason:

Adds unnecessary complexity initially.

---

# Consequences

Positive:

- Cleaner project.
- Better scalability.
- Easier testing.

Negative:

- More Gradle configuration.
- More initial setup.

---

# Future Impact

Modules should be introduced when they provide clear value.

Avoid unnecessary fragmentation.

---
Last updated: July 26, 2026
