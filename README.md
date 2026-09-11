# PantryHub

PantryHub is an offline-first Android shopping-list and pantry app. It keeps a reusable product catalog (with categories and search aliases), lets you build shopping lists from it, and guides you through the shop with a dedicated shopping mode. All data lives on the device; import/export gives you a portable JSON backup.

Version **1.0 is a personal, offline app**. Accounts and multi-device sync (1.1) and household sharing / QR invitations (1.2) are planned but deferred until shared server infrastructure exists. Progress is tracked in [`docs/STATUS.md`](docs/STATUS.md) (single source of truth) and the plan in [`docs/20_Rework_Plan.md`](docs/20_Rework_Plan.md).

---

## Features (1.0, implemented)

- Product catalog: create, edit, delete, search, favorites, duplicate detection.
- Categories with color, plus browsing/filtering by category.
- Product aliases (alternative names used by search and import matching).
- Shopping lists, including one-off / provisional lists and cloning from a base list.
- Shopping mode: check items off as you shop, then finish the list.
- Notes (create, edit, delete; included in backups).
- Import / export of the full dataset as JSON, with import preview and conflict handling.
- Settings: light/dark/system theme, dynamic color, in-app language (English, Spanish).
- In-app help.

Bottom navigation: **Lists / Products / Notes / Settings**.

---

## Tech stack

- Kotlin 2.0.21, Java 21 toolchain
- Jetpack Compose + Material 3
- MVVM + Clean Architecture (single `UiState` per screen, sealed intents)
- Hilt (dependency injection)
- Room (local database, version 4, explicit migrations)
- DataStore (preferences)
- Navigation Compose with type-safe routes
- kotlinx.serialization (JSON) and kotlinx-datetime
- Kotlin Coroutines / Flow
- minSdk 28, targetSdk / compileSdk 35
- Gradle Kotlin DSL with a version catalog (`gradle/libs.versions.toml`)

---

## Module layout

```text
PantryHub
├── app                    # Application entry point, navigation host, Hilt root
├── core-model             # Domain models (pure Kotlin)
├── core-common            # Shared utilities, result/error types, dispatchers
├── core-database          # Room database, entities, DAOs, migrations
├── core-data              # Repository implementations (offline), mappers, DI bindings
├── core-domain            # Repository interfaces and use cases
├── core-designsystem      # Theme, typography, colors and reusable Compose components
├── core-navigation        # Type-safe route definitions shared by features
├── feature-shopping       # Shopping lists, list detail, shopping mode
├── feature-products       # Product catalog and categories
├── feature-notes          # Notes
├── feature-settings       # Settings and help
├── feature-importexport   # JSON import / export
├── docs                   # Project documentation
└── gradle                 # Wrapper and version catalog
```

Dependency direction: feature modules depend on `core-*` modules; features never depend on each other; `app` depends on everything.

---

## Building

1. Open the project in Android Studio (a recent stable release with AGP 9.x support) using JDK 21.
2. Let Gradle sync (the version catalog resolves all dependencies).
3. Select the `app` run configuration and run on a device or emulator with API 28 or higher.

From the command line: `./gradlew assembleDebug` (or `gradlew.bat assembleDebug` on Windows).

---

## Documentation

- [`docs/README.md`](docs/README.md) — index of all documents (00–20) and ADRs.
- [`docs/STATUS.md`](docs/STATUS.md) — **single source of truth** for what is done, in progress and pending.
- [`docs/20_Rework_Plan.md`](docs/20_Rework_Plan.md) — the approved plan from the current state to 1.0 and beyond.
- [`docs/18_Changelog.md`](docs/18_Changelog.md) — version history.

Development is documentation-first: product and technical decisions are recorded in `docs/` (and `docs/decisions/` as ADRs) before or alongside implementation. If a document contradicts `STATUS.md`, `STATUS.md` wins.

---

## Language rule

All repository content — code, comments, commit messages and documentation — is written in **English**. User-facing strings are localized (English and Spanish) through Android resources.

---

## License

License information will be defined before the first public release. Bundled fonts are licensed under the SIL Open Font License (see `docs/licenses/`).

---
Last updated: September 11, 2026
