# ADR-002: Use Room as Local Database

## Status

Accepted

## Date

2026-01-01

---

# Context

PantryHub must work reliably without internet connection.

The application needs persistent storage for:

- Products.
- Categories.
- Shopping lists.
- Shopping items.
- Notes.
- Purchase history.

A structured local database is required.

---

# Decision

PantryHub will use:

```
Room Database
```

as the local persistence layer.

---

# Reasons

## Android Native Integration

Room is officially supported by Android.

Advantages:

- SQLite abstraction.
- Compile-time validation.
- Integration with Kotlin.

---

## Offline First Support

Room allows:

```
Database

↓

Repository

↓

ViewModel

↓

UI
```

without requiring network availability.

---

## Migration Support

The application will evolve.

Room provides:

- Schema migrations.
- Version control.
- Migration testing.

---

# Alternatives Considered

## Raw SQLite

Rejected.

Reasons:

- More boilerplate.
- Manual mapping.
- Higher maintenance.

---

## Realm

Rejected.

Reasons:

- Less aligned with Android ecosystem.
- Different persistence model.

---

## DataStore

Rejected as main database.

Reason:

Suitable for preferences, not relational data.

---

# Consequences

Positive:

- Reliable local storage.
- Strong typing.
- Easy testing.
- Good Kotlin support.

Negative:

- Requires migration management.
- Relational modeling must be designed carefully.

---

# Future Impact

Room will remain the local source of truth even when synchronization exists.

Future architecture:

```
Remote API

↓

Repository

↓

Room

↓

UI
```

---
Last updated: July 26, 2026
