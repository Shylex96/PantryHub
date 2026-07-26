# ADR-009: Use Kotlin Coroutines and Flow

## Status

Accepted

## Date

2026-01-01

---

# Context

PantryHub requires asynchronous operations:

- Database queries.
- Search.
- Import/export.
- Future synchronization.
- Background tasks.

The application needs reactive state handling.

---

# Decision

PantryHub will use:

```
Kotlin Coroutines

+

Flow
```

for asynchronous programming.

---

# Reasons

## Kotlin Native Solution

Coroutines are officially supported by Kotlin.

---

## Structured Concurrency

Operations respect lifecycle scopes.

Examples:

```
ViewModelScope

LifecycleScope
```

---

## Reactive Data

Flow allows:

```
Database change

↓

Flow emission

↓

UI update
```

---

# Usage Examples

Room:

```
Flow<List<Product>>
```

ViewModel:

```
StateFlow<UiState>
```

---

# Alternatives Considered

## RxJava

Rejected.

Reason:

Coroutines and Flow provide a simpler modern Kotlin approach.

---

## Callbacks

Rejected.

Reason:

Harder to maintain and compose.

---

# Consequences

Positive:

- Reactive UI.
- Lifecycle aware.
- Less callback complexity.

Negative:

- Requires understanding coroutine cancellation.

---

# Future Impact

All asynchronous operations should use:

```
Suspend functions

+

Flow

+

StateFlow
```

---
Last updated: July 26, 2026
