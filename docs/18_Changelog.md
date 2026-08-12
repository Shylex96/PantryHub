# PantryHub Changelog

> ⚠️ **Stale content.** The entries below are mostly fictional ("TBD" dates,
> "Development"/"Release Candidate" states for already-finished modules). They are not a
> reliable record. The **single source of truth for progress is `STATUS.md`**. From now on,
> each sprint closed in `19_Execution_Plan.md` produces a real entry here (with a real date
> and user-visible changes), and the previous noise is removed.

## Overview

This document contains the history of changes made to PantryHub.

The changelog records:

- New features.
- Improvements.
- Bug fixes.
- Technical changes.
- Database migrations.
- Important user-facing changes.

Each released version should have an entry.

---

# Version Format

Each version follows:

```
## Version X.Y.Z - YYYY-MM-DD
```

Sections:

```
Added

Changed

Fixed

Removed

Technical
```

---

# Unreleased

## Added

- **Visual identity (Sprint 1):** dual-mode theme — dark "Nocturne" (Night Owl) and light
  "Warm Pantry" — with a full Material 3 color role set, custom bundled fonts (Space Grotesk
  + Inter), and per-category colors. `dynamicColor` disabled by default.
- **Categories (Sprint 2):** full data + domain layer (DAO, repository, use cases with
  duplicate detection) and UI inside Products — filter chips, a manager dialog
  (create/rename/delete), category color dots, category assignment on create, and editing an
  existing product's category by tapping it.
- **Product aliases (Sprint 2b):** products can have alternative names ("papa" → "Patata")
  that feed search; editable from the product edit dialog.
- **Import / Export (Sprint 3):** export the whole database (products, categories, lists +
  items, purchase headers) to a JSON file via the system file picker, and import/merge a
  JSON backup with id remapping so foreign keys always hold. Lives in the Settings tab.
  Import shows a **preview** (counts) and flags **near-duplicate products** by Jaro-Winkler
  similarity (threshold 0.8, e.g. "Lentejas" vs "Lenteja") for the user to resolve as the
  same item or a new one. Exports are written truncated ("wt") to avoid stale trailing bytes.

## Changed

- Replaced hardcoded favorite/delete colors in the product and list screens with theme
  tokens (`extendedColors.favorite`, `colorScheme.error`).

## Technical

- **Room migration v2 → v3:** adds `aliases` and `normalized_aliases` columns to `products`
  (additive, existing data preserved).
- Reconciled documentation (Sprint 0): `STATUS.md` is the single source of truth; roadmap,
  backlog and this changelog were corrected.

---

## Changed

Pending improvements.

---

## Fixed

Pending fixes.

---

## Technical

Pending technical changes.

---

# Version 0.1.0 - Initial Foundation

Date:

```
2026-07-26
```

Status:

```
Released (v0.1.0-foundation)
```

---

## Added

Initial project foundation.

Created:

- Android application with hierarchical modular architecture.
- Full Hilt and KSP integration.
- Centralized Design System in `core-designsystem`.
- Type-safe navigation using Navigation Compose.
- Complete documentation system (Vision, Architecture, Roadmap, ADRs).

---

## Technical

Configured:

- Gradle 9.6.1 and AGP 9.3.1.
- Kotlin 2.0.21 and Compose BOM 2026.02.01.
- `kotlinx-datetime` integration.
- String normalization utilities for domain logic.

---

# Version 0.2.0 - Core Architecture

Date:

```
TBD
```

Status:

```
Development
```

---

## Added

Implemented:

- Clean Architecture structure.
- MVVM foundation.
- Hilt dependency injection.
- Room database foundation.
- Repository pattern.

---

## Technical

Created:

- Domain layer.
- Data layer.
- Presentation layer.

---

# Version 0.3.0 - Product Management

Date:

```
TBD
```

Status:

```
Development
```

---

## Added

Product management system.

Features:

- Create products.
- Edit products.
- Delete products.
- Product categories.
- Favorites.

---

## Changed

Improved:

- Product organization.
- Product discovery.

---

# Version 0.4.0 - Shopping Lists

Date:

```
TBD
```

Status:

```
Development
```

---

## Added

Shopping list management.

Features:

- Create lists.
- Edit lists.
- Delete lists.
- Duplicate lists.
- Add products to lists.

---

## Technical

Added:

- Shopping list domain model.
- Shopping item persistence.

---

# Version 0.5.0 - Search Experience

Date:

```
TBD
```

Status:

```
Development
```

---

## Added

Product search system.

Features:

- Instant search.
- Alphabetical ordering.
- Frequency ordering.
- Category filtering.

---

## Technical

Implemented:

- Product normalization.
- Search indexing.

---

# Version 0.6.0 - Shopping Mode

Date:

```
TBD
```

Status:

```
Development
```

---

## Added

Shopping mode.

Features:

- Mark products as purchased.
- Move completed items below pending items.
- Finish shopping session.

---

## Changed

Improved:

- Buying experience.
- List interaction.

---

# Version 0.7.0 - Purchase History

Date:

```
TBD
```

Status:

```
Development
```

---

## Added

Purchase tracking foundation.

Features:

- Purchase completion.
- Purchase date.
- Supermarket information.
- Total amount.

---

## Technical

Added:

- Purchase entities.
- Historical data support.

---

# Version 0.8.0 - Import / Export

Date:

```
TBD
```

Status:

```
Development
```

---

## Added

Data portability system.

Features:

- Export JSON.
- Import JSON.
- Schema versioning.
- Duplicate detection.
- Merge assistance.

---

## Technical

Added:

- Serialization system.
- Import validation.
- Migration support.

---

# Version 0.9.0 - Polish Release

Date:

```
TBD
```

Status:

```
Release Candidate
```

---

## Added

Final preparation features:

- Dark mode.
- Localization.
- Accessibility improvements.
- UI refinements.

---

## Changed

Improved:

- Animations.
- Navigation.
- User experience.

---

# Version 1.0.0 - First Stable Release

Date:

```
TBD
```

Status:

```
Released
```

---

# Added

Initial public version of PantryHub.

Includes:

## Shopping Management

- Create shopping lists.
- Add products.
- Search products.
- Categories.
- Favorites.

---

## Shopping Experience

- Shopping mode.
- Item completion.
- Purchase completion.

---

## Personal Data

- Notes.
- Purchase history foundation.
- JSON export/import.

---

## User Experience

- Material 3 design.
- Light theme.
- Dark theme.
- Localization support.

---

# Changed

Final improvements before release:

- Performance optimization.
- UI refinement.
- Improved navigation.

---

# Fixed

Resolved:

- Known stability issues.
- Database inconsistencies.
- UI edge cases.

---

# Technical

Release includes:

- Stable database version.
- Production configuration.
- Release signing.
- Automated testing pipeline.

---

# Future Versions

Future changelog sections may include:

---

# Version 1.1.0

Potential features:

- Advanced reports.
- Price tracking.
- Better purchase analysis.

---

# Version 1.2.0

Potential features:

- Household preparation.
- Improved sharing.

---

# Version 2.0.0

Potential features:

- Cloud synchronization.
- Household accounts.
- Real-time collaboration.

---

# Changelog Rules

Every release must:

- Have a version number.
- Include release date.
- Describe user-visible changes.
- Mention important technical changes.

---

# Final Purpose

The changelog exists to preserve the evolution of PantryHub.

It should answer:

```
What changed?

Why did it change?

How does it improve PantryHub?
```

---
Last updated: July 26, 2026
