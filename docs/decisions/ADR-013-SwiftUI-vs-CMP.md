# ADR-013: Native SwiftUI over Compose Multiplatform

## Status
Proposed — 2026-09-13

## Context
With shared Kotlin logic in place, the iOS UI can either reuse the Compose UI through Compose
Multiplatform or be written natively in SwiftUI. `core-designsystem` is 29 files of Compose
Material 3 built around a deliberate dual-identity theme (Nocturne / Warm Pantry), and the
five feature modules are 32 more files of Compose. Reusing them is tempting.

`09_Design_System.md` §1 and `04_UX_Guidelines.md` both commit to an app that feels native.
Material 3 rendered on iOS does not feel like iOS: the navigation model, the sheets, the back
gesture, the type and the system integrations all read as Android.

## Decision
**Android keeps Jetpack Compose + Material 3. iOS gets SwiftUI.** No Compose Multiplatform.

Shared: state shapes, use cases, repository contracts, validation, business rules.
Not shared: components, layout, navigation, animation.

The design system is shared at the level of *decisions*, not code — the palettes of
`05_Design_System.md` §2, the spacing and radius scales, the category colors and the naming.
The iOS implementation is a parallel SwiftUI design system reading the same tokens, so the two
apps are coherent without being identical.

Revisit only if a concrete, measured problem appears (for example, the two design systems
visibly diverging), not to avoid writing screens twice.

## Alternatives
- **Compose Multiplatform for iOS.** Rejected for 1.0: it buys screen reuse at the cost of the
  native feel that is the reason for building a separate iOS app, and it would pull the whole
  Compose stack into the shared modules.
- **A shared UI in the future.** Left open. The architecture in ADR-012 does not prevent it —
  nothing about sharing model/domain/data commits the UI layer either way.

## Consequences

### Positive
- Each platform feels like itself: `NavigationStack`, native sheets, native gestures.
- The shared modules stay small and free of UI dependencies, which keeps the iOS framework
  small and the compile times honest.
- Android development is entirely unaffected.

### Negative
- Every screen is implemented twice. Phase 10 is 8–14 sessions for exactly this reason.
- Visual coherence depends on discipline, not on a compiler.
- Two design systems to maintain.

---
Last updated: September 13, 2026
