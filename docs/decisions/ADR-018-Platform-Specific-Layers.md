# ADR-018: Navigation, design system and ViewModels stay per-platform

## Status
Proposed — 2026-09-13

## Context
Once model, domain and data are shared, the boundary question is what happens immediately
above them: navigation, the design system, and the state holders between use cases and UI.

`core-navigation` is 2 files built on `NavHostController`. `core-designsystem` is 29 Compose
files. Each feature has a Hilt `ViewModel` exposing a single `UiState` and consuming sealed
intents. `androidx.lifecycle.ViewModel` does now have multiplatform artifacts, so sharing the
ViewModels is technically possible.

## Decision
**All three stay per-platform.**

- **Navigation.** Android keeps Navigation Compose; iOS uses `NavigationStack`. `NavController`
  never enters `commonMain`. What *is* shared is identifiers and route arguments — a
  `ShoppingListId` is shared; the graph is not.
- **Design system.** Per ADR-013. Shared at the level of tokens and naming
  (`05_Design_System.md` §2–§4), not code.
- **ViewModels.** Not shared in this plan. Android keeps `ViewModel` + `hiltViewModel()`; iOS
  uses `@Observable`. Both call the same use cases. Today's ViewModels are thin — they hold a
  `StateFlow<UiState>` and dispatch intents — so sharing them would buy little and would pull
  a lifecycle dependency into shared code while making the iOS side fight Kotlin's
  `StateFlow`-to-Swift ergonomics.

If a screen's *reducer* turns out to be non-trivial and identical on both platforms, the unit
to share is that reducer — a pure `(State, Action) -> State` function in `commonMain` — not
the ViewModel around it. Decide that per screen, with the screen in front of you, in Phase 10.

## Alternatives
- **Share the ViewModels via multiplatform lifecycle.** Rejected for now: real cost (lifecycle
  dependency in shared code, `StateFlow` interop friction on Swift) against a thin layer's
  worth of benefit. Revisit if Phase 10 shows genuinely heavy, duplicated state logic.
- **Share navigation.** Rejected: forces one platform's navigation model onto the other, and
  contradicts ADR-013.

## Consequences

### Positive
- Each platform integrates natively with its own lifecycle, back behaviour and state
  restoration.
- Shared modules stay free of lifecycle and UI dependencies.
- The door to sharing reducers later stays open; nothing here forecloses it.

### Negative
- State-holder logic is written twice, and can drift.
- The mitigation is that it must stay thin: any rule that appears in a ViewModel belongs in a
  use case instead. Code review enforces this.

---
Last updated: September 13, 2026
