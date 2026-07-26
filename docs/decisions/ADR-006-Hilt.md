# ADR-006: Use Hilt for Dependency Injection

## Status

Accepted

## Date

2026-01-01

---

# Context

PantryHub will contain multiple layers and components:

- ViewModels.
- Use Cases.
- Repositories.
- Database.
- Network clients.
- Synchronization services.

Manually creating and passing dependencies would increase coupling and complexity.

---

# Decision

PantryHub will use:

```
Hilt
```

as the dependency injection framework.

---

# Reasons

## Official Android Integration

Hilt is built on top of:

```
Dagger
```

and is officially recommended by Android.

---

## Lifecycle Awareness

Hilt integrates with:

- Activities.
- Fragments.
- ViewModels.
- WorkManager.

---

## Testability

Dependencies can easily be replaced.

Example:

Production:

```
RealProductRepository
```

Testing:

```
FakeProductRepository
```

---

# Architecture Example

Without Hilt:

```
ViewModel

creates

Repository

creates

Database
```

High coupling.

---

With Hilt:

```
ViewModel

receives

Repository

receives

Database
```

---

# Alternatives Considered

## Manual Dependency Injection

Rejected.

Reason:

Too much boilerplate as the application grows.

---

## KOIN

Rejected.

Reason:

Although simpler, Hilt provides stronger Android integration and compile-time validation.

---

# Consequences

Positive:

- Cleaner dependency management.
- Better testing.
- Less boilerplate.
- Official Android support.

Negative:

- Requires annotation processing.
- Adds initial configuration complexity.

---

# Future Impact

All application dependencies should be provided through Hilt modules.

Examples:

```
DatabaseModule

NetworkModule

RepositoryModule
```

---
Last updated: July 26, 2026
