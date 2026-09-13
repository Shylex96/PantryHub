# ADR-012: Kotlin Multiplatform adoption strategy

## Status
Proposed — 2026-09-13

## Context
PantryHub is Android-only. We want an iOS app without a second implementation of the business
rules and without pausing Android development. The audit in `docs/22_KMP_iOS_Migration_Plan.md`
found the codebase unusually well positioned: `core-model` has zero platform imports,
`core-domain` imports no Dagger/Hilt at all (only `javax.inject.Inject`), the repository
boundary is already a set of interfaces, every entity has a UUID id, and there is no
networking layer to port.

It also found a blocker the migration brief did not anticipate: the project runs Kotlin 2.0.21
and KSP 2.0.21-1.0.28 (October 2024) under AGP 9.3.1 and Gradle 9.6.1. Under AGP 9 a KMP
library module must use `com.android.kotlin.multiplatform.library`, which wants KGP ≥ 2.0
(2.3+ recommended) and KSP ≥ 2.3.1.

The alternatives considered were a native Swift rewrite of the domain, a cross-platform
rewrite (Flutter / React Native), and Kotlin Multiplatform.

## Decision
Adopt **Kotlin Multiplatform** for model, domain, data and database, incrementally, in the
phase order of `22_KMP_iOS_Migration_Plan.md` §5.

Two things are prerequisites and ship on their own merit, before any source set moves:

1. **Phase 1 — toolchain.** Kotlin → 2.3.x, KSP → 2.3.x, dependencies current, and the unused
   Retrofit/OkHttp/Coil/WorkManager declarations deleted. Released as a normal Android update.
2. **Phase 2 — dependency inversion.** Repository interfaces move from `core-data` to
   `core-domain`, so the domain stops transitively depending on Room. `07_Architecture.md`
   already claims this is the case; it is not.

Android must compile, run and pass its tests after every phase. Any phase that cannot is
reverted rather than patched forward.

## Alternatives
- **Rewrite the domain in Swift.** Rejected: duplicates every business rule and guarantees the
  two platforms drift. This is the failure mode the whole plan exists to avoid.
- **Flutter / React Native.** Rejected: discards a working, well-structured Android app and
  contradicts the native-feel goal of `02_Product_Principles.md`.
- **Do nothing / stay Android-only.** Legitimate, and Phases 1 and 2 remain worth doing on
  their own if this is chosen.

## Consequences

### Positive
- Business rules exist once and are tested once, in `commonTest`.
- Phases 1 and 2 improve the Android app whether or not iOS ever ships.
- The architecture stays modular; the boundaries that already exist are what make this cheap.

### Negative
- A Kotlin 2.0 → 2.3 jump with Hilt, KSP and Compose attached carries real risk (R1).
- iOS targets cannot be built on a Windows host at all; a Mac or a macOS CI runner is required
  from Phase 4 (R4).
- Build times grow with each additional target.
- 27–41 working sessions to a working iOS app with one real screen.

---
Last updated: September 13, 2026
