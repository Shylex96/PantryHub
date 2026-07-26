# ADR-007: Use Gradle Version Catalog

## Status

Accepted

## Date

2026-01-01

---

# Context

PantryHub will use multiple dependencies:

- Compose.
- Hilt.
- Room.
- Retrofit.
- Testing libraries.

Managing versions directly inside Gradle files becomes difficult over time.

---

# Decision

PantryHub will use:

```
Gradle Version Catalog
```

through:

```
libs.versions.toml
```

---

# Reasons

## Centralized Dependencies

All versions exist in one place.

Example:

```
libs.versions.toml
```

contains:

```
compose = "x.x.x"

room = "x.x.x"

hilt = "x.x.x"
```

---

## Easier Updates

Updating a dependency requires changing one location.

---

## Better Project Consistency

All modules share the same versions.

---

# Alternatives Considered

## Hardcoded Gradle Versions

Rejected.

Problems:

- Duplication.
- Version conflicts.
- Difficult maintenance.

---

## External Build Logic

Possible in the future.

Not required initially.

---

# Consequences

Positive:

- Cleaner Gradle files.
- Easier dependency upgrades.
- Better multi-module support.

Negative:

- Initial setup required.

---

# Future Impact

All new dependencies must be added through:

```
libs.versions.toml
```

and never directly inside modules.

---
Last updated: July 26, 2026
