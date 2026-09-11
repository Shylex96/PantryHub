# PantryHub Roadmap

> **Reconciled on 2026-09-11 — `STATUS.md` is authoritative.** This roadmap was aligned
> with the product decision of 2026-09-11: **1.0 is the offline personal app**, **1.1 adds
> accounts + personal multi-device sync** (deferred until shared server infrastructure
> exists), **1.2 adds households / sharing / QR** (deferred). The detailed plan is in
> `20_Rework_Plan.md`; the execution order of the pre-1.0 sprints is in
> `19_Execution_Plan.md`. This document is a long-term vision (which versions will exist
> and their scope), not a status record — for progress, `STATUS.md` wins.

## Overview

This document defines the long-term evolution plan of PantryHub.

The roadmap is organized into incremental milestones. Each milestone delivers a complete, functional improvement while preserving a clean architecture, offline-first philosophy, and long-term maintainability.

---

# Development Philosophy

PantryHub follows these core principles throughout development:

- Offline-first by design.
- Clean Architecture with strict module separation.
- Simplicity before complexity.
- Reusable components over duplicated implementations.
- Stable data model before adding features.
- Build scalable foundations before polishing.
- User experience is driven by usability rather than visual effects.
- Preserve backward compatibility whenever possible.

---

# Version Roadmap

---

# v0.1.0-foundation

## Objective

Establish the technical foundation of the project.

## Status

Completed

## Scope

- Multi-module architecture
- Clean Architecture
- Dependency Injection (Hilt)
- Jetpack Compose
- Material 3
- Navigation
- Documentation
- ADR structure
- Version catalog
- Project conventions

---

# v0.2.0-domain

## Objective

Define the application's business model independently from persistence and UI.

## Status

Completed

## Scope

- Domain models
- Business rules
- Shopping use cases
- Product use cases
- Duplicate detection
- Search logic
- Shopping list lifecycle
- Domain validation

---

# v0.3.0-storage

## Objective

Implement persistent offline storage.

## Status

Completed

## Scope

- Room database
- Entities
- DAOs
- Repository implementations
- Mappers
- Offline-first persistence
- Dependency Injection integration

---

# v0.4.0-shopping-flow

## Objective

Create the first complete shopping experience.

## Status

Completed

## Scope

- Shopping Lists screen
- Shopping List Detail screen
- Shopping Mode
- Navigation flow
- ViewModels
- State management
- Interactive shopping experience

---

# v0.5.0-persistence

## Objective

Replace temporary data with real persistent storage.

## Status

Completed

## Scope

- Repository integration
- Reactive Flow observation
- Persistent shopping lists
- Persistent shopping items
- Shopping completion workflow
- Removal of mock data

---

# v0.5.1-design-system-foundation

## Objective

Create the visual foundation that every future feature will reuse.

## Status

Completed

## Scope

### Theme

- Color palette
- Light Theme
- Dark Theme
- Typography
- Shapes
- Dimensions
- Spacing system

### Components

- PantryButton
- PantryCard
- PantryTopBar
- PantrySearchBar
- PantryListItem
- PantryLoading
- PantryEmptyState
- PantryErrorState

### UX Foundation

- Accessibility improvements
- Material 3 consistency
- Preview support
- Internationalization preparation
- Shared visual language

---

# v0.6.0-ui-refresh

## Objective

Apply the new Design System across the application.

## Status

Completed for the Shopping section (VP-0..VP-2); Products, Notes and Settings pending (VP-3..VP-5, rework phase R2). See `STATUS.md`.

## Scope

- Shopping Lists redesign
- Shopping Detail redesign
- Shopping Mode redesign
- Better spacing
- Better hierarchy
- Improved navigation
- Empty states
- Loading states
- Error states
- Micro-interactions
- UX improvements

Result:

The application should look and feel like a production-ready Android application.

---

# v0.7.0-products

## Objective

Implement the complete reusable product catalog.

## Status

Completed (statistics and recently/frequently used remain post-1.0 ideas)

## Scope

- Product CRUD
- Categories
- Favorites
- Product search
- Smart filtering
- Recently used products
- Frequently used products
- Duplicate prevention
- Product statistics

---

# v0.8.0-import-export

## Objective

Provide complete data portability.

## Status

Completed

## Scope

- JSON export
- JSON import
- Merge strategies
- Conflict resolution
- Backup
- Restore
- Import preview

---

# v0.9.0-notes-and-history

## Objective

Expand shopping information beyond products.

## Status

Notes completed. Purchase history is not part of 1.0 (moved to 1.3+).

## Scope

### Notes

- Shopping notes
- Product notes
- List notes

### History

- Completed purchases
- Purchase dates
- Stores
- Prices
- Shopping history

---

# v1.0.0-offline

## Objective

Ship PantryHub as a complete **offline personal app**. No accounts, no server, no sharing.

## Status

In progress (rework phases R1–R3, see `20_Rework_Plan.md` and `STATUS.md`)

## Scope

- Products, categories and aliases
- Shopping lists, including one-off / provisional lists and cloning from a base list
- Shopping mode
- Notes
- Import / export (JSON)
- Settings: theme (light / dark / system), dynamic color, in-app language
- Help
- Visual pass complete on all sections (VP-3, VP-4, VP-5)
- Safe Room migrations (no destructive fallback in release builds; migration tests)
- Unit / migration / Compose smoke tests
- Icon, splash, versioning, signing, Play listing (Internal / Beta)

---

# v1.1.0-connected

## Objective

Add user accounts and **personal multi-device synchronization** for a single user.

## Status

Deferred (rework phase R4) until the shared server infrastructure exists.

## Scope

- Sync-ready data model (Room v5: `updated_at` and `deleted_at` on all synced entities, soft delete, outbox)
- Authentication and accounts
- Personal multi-device sync with conflict resolution
- Remote backup

---

# v1.2.0-households

## Objective

Transform PantryHub into a collaborative household application.

## Status

Deferred (rework phase R5), after 1.1.

## Scope

- Shared households
- Shared shopping lists
- Shared products
- User roles
- Invitations
- QR pairing
- Shared-data synchronization and conflict resolution

---

# Future Versions (1.3+)

Potential later milestones, not yet scheduled:

## v1.3.0-analytics

- Purchase history (completed purchases, dates, stores, prices)
- Spending reports
- Purchase frequency
- Monthly statistics
- Product trends
- Store comparison
- Dashboards

---

## v1.4.0-price-tracking

- Product price history
- Average price
- Best historical price
- Price alerts
- Price evolution

---

## v1.5.0-recipes

- Recipe management
- Ingredient lists
- Add recipe ingredients to shopping list
- Pantry integration

---

## v1.6.0-smart-shopping

- Shopping suggestions
- Frequently forgotten products
- Habit detection
- Consumption prediction
- Intelligent recommendations

---

## v1.7.0-polish

- Performance optimization
- Tablet support
- Landscape support
- Widgets
- Notifications

---

# Milestones Summary

| Version | Description | Status |
|----------|-------------|-----------|
| v0.1.0 | Foundation | Completed |
| v0.2.0 | Domain | Completed |
| v0.3.0 | Storage | Completed |
| v0.4.0 | Shopping Flow | Completed |
| v0.5.0 | Persistence | Completed |
| v0.5.1 | Design System (structure) | Completed |
| v0.7.0 | Products | Completed |
| v0.6.0 | Visual identity (UI Refresh) | Completed (Sprint 1); visual pass VP-3..5 pending |
| — | Categories (management + browsing) | Completed (Sprint 2) |
| — | Product aliases | Completed (Sprint 2b) |
| v0.8.0 | Import & Export (UI) | Completed (Sprint 3) |
| v0.9.0 | Notes | Completed (Sprint 4) |
| — | Settings | Completed (Sprint 5) |
| — | Templates / provisional lists | Completed (Sprint 6) |
| v1.0.0 | Offline personal app: visual pass + QA + release | In progress (R1–R3) |
| v1.1.0 | Accounts + personal multi-device sync | Deferred (R4) |
| v1.2.0 | Households / sharing / QR | Deferred (R5) |
| v1.3.0+ | Analytics, price tracking, recipes, smart shopping, widgets / tablet | Ideas |

> Live detail in `STATUS.md`.

---

Last updated: September 11, 2026