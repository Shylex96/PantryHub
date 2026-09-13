# ADR-014: Convert modules in place, plus one iOS umbrella

## Status
Proposed — 2026-09-13

## Context
Two shapes were considered for the shared code:

- **A.** Convert the existing `core-model`, `core-common`, `core-domain`, `core-data` and
  `core-database` modules into Kotlin Multiplatform modules in place.
- **B.** Create a new `shared` (or `sharedLogic`) module and migrate code into it progressively.

Swift, meanwhile, wants to write one `import`, not five — a multi-module KMP project needs
something that assembles a single framework.

## Decision
**Option A, plus a thin `:shared-ios` umbrella.**

The five modules become KMP libraries in place, keeping their names, their responsibilities and
the dependency rules of `07_Architecture.md`. `:shared-ios` contains **no source code**: it
declares the iOS framework target and `export`s `core-model`, `core-common`, `core-domain` and
`core-data`, producing one `PantryHubShared.xcframework`.

Integration with Xcode is local (a Gradle-produced framework referenced by the Xcode project).
No artifact publishing until there is a reason for it.

## Alternatives
- **Option B (one `shared` module).** Rejected: the 7-module `core-*` split with enforced
  dependency rules is the single best structural property of this codebase and the reason the
  migration is cheap at all. Collapsing it into one module trades that for a marginally
  simpler Gradle setup.
- **A without an umbrella** (Swift importing several frameworks). Rejected: exposes the Kotlin
  module layout as the Swift API surface, and makes every internal reshuffle a breaking change
  for the iOS app.

## Consequences

### Positive
- Module boundaries, and the rules that protect them, survive the migration intact.
- Each module can be converted and verified independently — one phase, one module, one commit.
- Swift sees one stable framework regardless of how the Kotlin side is organised.

### Negative
- Five `build.gradle.kts` files must be migrated to `com.android.kotlin.multiplatform.library`
  (the `android { }` block is replaced by `kotlin { androidLibrary { } }`), and KSP must be
  registered per target.
- One extra module to maintain.
- More targets across more modules means longer builds.

---
Last updated: September 13, 2026
