# PantryHub Roadmap

> ⚠️ **Stale states and order.** The version numbering below does not match the real order
> of work, and some states are false (e.g. "v0.6 UI Refresh: Completed" — the visual
> identity is exactly what is MISSING). The **single source of truth for progress is
> `STATUS.md`** and the execution order is set by `19_Execution_Plan.md`. This roadmap is
> kept as a long-term vision (which versions will exist and their scope), not as a status
> record.

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

Planned

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

Planned

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

# v1.0.0-community

## Objective

Transform PantryHub into a collaborative household application.

## Scope

- Shared households
- Shared shopping lists
- Shared products
- User roles
- Invitations
- QR pairing
- Synchronization
- Conflict resolution

---

# v1.1.0-analytics

## Objective

Generate useful insights from shopping history.

## Scope

- Spending reports
- Purchase frequency
- Monthly statistics
- Product trends
- Store comparison
- Price evolution
- Dashboards

---

# v1.2.0-polish

## Objective

Finalize the product for public release.

## Scope

- Performance optimization
- Accessibility review
- Tablet support
- Landscape support
- Widgets
- Notifications
- Final animations
- Play Store assets
- Production QA

---

# Future Versions

Potential future milestones include:

## v1.3.0-price-tracking

- Product price history
- Average price
- Best historical price
- Price alerts
- Price evolution

---

## v1.4.0-recipes

- Recipe management
- Ingredient lists
- Add recipe ingredients to shopping list
- Pantry integration

---

## v1.5.0-smart-shopping

- Shopping suggestions
- Frequently forgotten products
- Habit detection
- Consumption prediction
- Intelligent recommendations

---

## v2.0.0-cloud

- Cloud synchronization
- Multi-device support
- Automatic backups
- Authentication
- Real-time synchronization

---

# Milestones Summary

| Version | Description | Status    |
|----------|-------------|-----------|
| v0.1.0 | Foundation | Completed |
| v0.2.0 | Domain | Completed |
| v0.3.0 | Storage | Completed |
| v0.4.0 | Shopping Flow | Completed |
| v0.5.0 | Persistence | Completed |
| v0.5.1 | Design System (structure) | Completed |
| v0.7.0 | Products | Completed |
| v0.6.0 | Visual identity (UI Refresh) | **Next (Sprint 1)** |
| — | Categories (management + browsing) | Sprint 2 |
| — | Product aliases | Sprint 2b |
| v0.8.0 | Import & Export (UI) | Sprint 3 |
| v0.9.0 | Notes | Sprint 4 |
| — | Settings | Sprint 5 |
| — | Templates / provisional lists | Sprint 6 |
| v1.0.0 | Close + QA + release | Planned |
| v1.1.0+ | Community / Analytics (post-1.0) | Planned |

> Table corrected. Live detail in `STATUS.md`.

---

Last updated: July 28, 2026