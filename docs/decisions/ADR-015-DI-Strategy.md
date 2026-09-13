# ADR-015: Dependency injection for KMP — constructors in shared code, Hilt only on Android

## Status
Proposed — 2026-09-13

## Context
`ADR-006` adopted Hilt, and it works well on Android. Hilt is Android-only and can never enter
`commonMain`.

The audit found the situation better than expected: **no file in `core-domain` imports
`dagger.*`.** All 36 files import exactly one DI symbol, `javax.inject.Inject`, on their
constructors. `core-data` has one Hilt file (`DataModule.kt`) and one Hilt-annotated
implementation (`DataStoreSettingsRepository.kt`); `core-database` has one (`DatabaseModule.kt`).
So "remove Hilt from the domain" is deleting 36 annotations, not a refactor.

`javax.inject` itself does not exist on Kotlin/Native, so the annotations must go regardless.

The options for shared code were: adopt a KMP DI framework (Koin, kotlin-inject), invent an
abstraction over both, or use plain constructors.

## Decision
**Shared code has no DI framework.** Use cases and repository implementations expose plain
constructors and receive their dependencies as parameters. Each platform composes the graph at
its own root:

```
             core-domain / core-data  ──▶  plain constructors, interfaces only
                         ▲                              ▲
      Android: Hilt @Module in :app          iOS: a small factory object in iosMain,
      provides the shared classes            called from Swift at app start
```

Android keeps Hilt exactly as it is today for ViewModels, Room and DataStore; the only change
is that a Hilt `@Module` now `@Provides` the shared use cases instead of Hilt discovering them
through `@Inject` constructors.

`Domain → Hilt` remains forbidden, in either direction.

## Alternatives
- **Koin in `commonMain`.** Rejected: adds a runtime dependency and a second DI model to a
  domain that currently needs neither. The brief's own rule — do not introduce a DI framework
  into the domain just to make the platforms symmetric — applies.
- **kotlin-inject / Metro.** Rejected for the same reason, with more compile-time cost.
- **Keep `javax.inject` annotations in shared code.** Not possible: unavailable on Native.

## Consequences

### Positive
- The domain depends on nothing but Kotlin, coroutines and its own interfaces — the most
  testable state it can be in.
- No DI framework to learn, version or debug on the iOS side.
- Android behaviour is unchanged.

### Negative
- 36 annotations to delete and one Hilt `@Module` to write, all in one commit (R5).
- Wiring is manual and grows with the number of use cases; if the composition root becomes
  unwieldy on either platform, revisit with a concrete example rather than pre-emptively.

---
Last updated: September 13, 2026
