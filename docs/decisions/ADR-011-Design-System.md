# ADR-011: Design System Foundation

## Status
Proposed

## Context
As PantryHub grows into a multi-module project, visual consistency across features becomes critical. Hardcoding dimensions, colors, and repeating component logic leads to technical debt and a fragmented user experience. We need a centralized Design System that provides both "Tokens" (values) and "Components" (UI blocks).

## Decision
We will establish a formal Design System foundation in the `:core-designsystem` module based on the following principles:

1. **Layered on Material 3**: We use Material 3 as the underlying foundation but wrap it with our own semantic layer.
2. **Token-Based Design**: All visual values (Spacing, Elevation, Icon Sizes, Typography) are defined as tokens in `PantryHubTheme`.
3. **Stateless Components**: Components in the design system must be "dumb" (stateless). They receive data via parameters and notify events via lambdas.
4. **Feature Agnostic**: No component in this module should reference specific feature logic (e.g., `ShoppingList`).
5. **Theme Provider**: We use `CompositionLocalProvider` to inject our custom tokens into the Compose tree.

## Consequences

### Positive
- **Consistency**: All screens will share the same "look and feel".
- **Scalability**: New features can be built rapidly using the provided component library.
- **Maintainability**: Changing a visual token (e.g., brand color) will propagate throughout the entire app.
- **Accessibility**: Standardized touch targets and contrast rules are baked into the core components.

### Negative
- **Indirection**: Developers must learn the `PantryHubTheme` tokens instead of using standard Material 3 or raw `dp` values.
- **Initial Overhead**: Creating generic wrappers takes more time initially than writing inline Compose code.

---
Last updated: July 27, 2026
