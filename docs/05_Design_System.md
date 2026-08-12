# PantryHub Design System

> The single source of truth for PantryHub's visual language: tokens, color, typography,
> shape, motion and the rules that keep every screen consistent, accessible and fast to
> build. All values here are implemented in `:core-designsystem`.
>
> Last updated: 2026-08-12

---

## 1. Identity: a dual-personality theme

PantryHub ships **two curated color identities**, one per mode — a deliberate choice, not
an automatic invert:

- **Dark mode — "Nocturne"**, referencing the *Night Owl* theme (VS Code / Postman): a deep
  navy-blue canvas with lavender/violet accents and a teal secondary. Calm, focused,
  premium; easy on the eyes at night.
- **Light mode — "Warm Pantry"**: a warm, organic palette (sage green, earthy neutral,
  soft terracotta) on off-white surfaces. Friendly and grounded; the app's daytime face.

Each mode is an independently designed Material 3 `ColorScheme`. The structure (which role
does what) is identical across modes; only the hues change. Favorites, status and category
colors are defined for both modes so meaning never depends on the mode.

**Dynamic color (Material You) is OFF by default** to preserve this identity. It is exposed
as an opt-in flag (`dynamicColor`) and will be surfaced as a Settings toggle later.

---

## 2. Color tokens

### 2.1 Dark — Nocturne (Night Owl)

| Role | Hex | Notes |
|---|---|---|
| primary | `#C792EA` | lavender — buttons, FAB, active states |
| onPrimary | `#24123A` | text/icon on primary |
| primaryContainer | `#4B3A7A` | |
| onPrimaryContainer | `#ECDDFF` | |
| secondary | `#82AAFF` | Night Owl blue — secondary accents |
| onSecondary | `#08224A` | |
| secondaryContainer | `#26406B` | |
| onSecondaryContainer | `#D8E4FF` | |
| tertiary | `#21C7A8` | teal — highlights |
| onTertiary | `#00382D` | |
| tertiaryContainer | `#005141` | |
| onTertiaryContainer | `#A9F0E0` | |
| background | `#011627` | app canvas |
| onBackground | `#D6DEEB` | primary text |
| surface | `#011627` | |
| onSurface | `#D6DEEB` | |
| surfaceVariant | `#0E2A3F` | |
| onSurfaceVariant | `#8FA9C4` | dimmed text / icons |
| surfaceContainerLowest | `#00101D` | |
| surfaceContainerLow | `#06203A` | |
| surfaceContainer | `#0B2942` | cards, app bar |
| surfaceContainerHigh | `#103450` | raised surfaces |
| surfaceContainerHighest | `#16405C` | |
| outline | `#3C5A74` | |
| outlineVariant | `#1E3648` | hairline borders |
| error | `#FF6B81` | |
| onError | `#400010` | |
| inverseSurface | `#D6DEEB` | |
| inversePrimary | `#6A4BB0` | |

### 2.2 Light — Warm Pantry (Sage / Earth / Terracotta)

| Role | Hex | Notes |
|---|---|---|
| primary | `#4C665C` | sage — buttons, FAB, active states |
| onPrimary | `#FFFFFF` | |
| primaryContainer | `#D6E4DA` | |
| onPrimaryContainer | `#0A1F17` | |
| secondary | `#625B51` | earthy neutral |
| onSecondary | `#FFFFFF` | |
| secondaryContainer | `#EBE3D5` | |
| onSecondaryContainer | `#201B14` | |
| tertiary | `#94544A` | terracotta |
| onTertiary | `#FFFFFF` | |
| tertiaryContainer | `#F7D6D0` | |
| onTertiaryContainer | `#3B0F09` | |
| background | `#FDFBFA` | app canvas |
| onBackground | `#1C1B1A` | primary text |
| surface | `#FDFBFA` | |
| onSurface | `#1C1B1A` | |
| surfaceVariant | `#F0EAE4` | |
| onSurfaceVariant | `#55504A` | dimmed text / icons |
| surfaceContainerLowest | `#FFFFFF` | |
| surfaceContainerLow | `#F8F3F0` | |
| surfaceContainer | `#F4F0EF` | cards, app bar |
| surfaceContainerHigh | `#EFE9E4` | |
| surfaceContainerHighest | `#E9E3DD` | |
| outline | `#857F78` | |
| outlineVariant | `#D8D0C8` | hairline borders |
| error | `#B3261E` | |
| onError | `#FFFFFF` | |
| inverseSurface | `#313030` | |
| inversePrimary | `#A6BCB1` | |

### 2.3 Extended colors (mode-aware, outside the M3 scheme)

Provided via `LocalPantryExtendedColors` and read as `PantryHubTheme.extendedColors`.

| Token | Dark | Light |
|---|---|---|
| favorite | `#ECC48D` | `#B7791F` |
| success | `#ADDB67` | `#2E7D32` |
| warning | `#F5B454` | `#B26A00` |
| onStatus (text on status fills) | `#04121F` | `#FFFFFF` |

**Category colors** (for the Categories feature and item dots):

| Category | Dark | Light |
|---|---|---|
| vegetables | `#ADDB67` | `#57A773` |
| fruit | `#F7B267` | `#E08A00` |
| dairy | `#82AAFF` | `#4C7DD9` |
| meat | `#FF9E80` | `#C0562F` |
| bakery | `#ECC48D` | `#B98A3E` |
| drinks | `#21C7A8` | `#0E8F79` |
| frozen | `#86CBED` | `#3E86A0` |
| household | `#C792EA` | `#8A5CC0` |
| other | `#8FA9C4` | `#6B7A8A` |

---

## 3. Typography

Custom type replaces the default Roboto to give the app character. **Bundled** (offline, no
Play Services dependency) as OFL fonts in `res/font`:

- **Display / headlines:** *Space Grotesk* (600/700) — geometric, modern, a touch technical.
- **Body / labels:** *Inter* (400/500/600) — highly legible at small sizes.

> Implemented: both families are bundled as OFL variable fonts in
> `core-designsystem/src/main/res/font/` (`space_grotesk.ttf`, `inter.ttf`) and wired in
> `Type.kt` via `FontVariation` (one file per family covers every weight). OFL licenses are
> kept in `docs/licenses/`.

Scale (Material 3 slots):

| Slot | Font | Weight | Size / Line |
|---|---|---|---|
| displayLarge | Space Grotesk | 700 | 32 / 40 |
| headlineMedium | Space Grotesk | 600 | 24 / 32 |
| titleLarge | Space Grotesk | 600 | 20 / 28 |
| titleMedium | Inter | 600 | 16 / 24 |
| bodyLarge | Inter | 400 | 16 / 24 |
| bodyMedium | Inter | 400 | 14 / 20 |
| labelLarge | Inter | 600 | 14 / 20 |
| labelMedium | Inter | 500 | 12 / 16 |

---

## 4. Shape, spacing, elevation

Shapes (`PantryShapes`): small `8dp`, medium `16dp`, large `24dp`. Cards use medium; FAB and
primary buttons use medium→large; text fields use small→medium.

Spacing (`PantrySpacing`): `xs 4 · sm 8 · md 12 · lg 16 · xl 24 · xxl 32`. Screen edges use
`lg`; grouping uses `md`; section separation uses `xl`.

Elevation (`PantryElevation`): `low 2 · medium 4 · high 8`. In dark mode, prefer **tonal
elevation** (lighter surfaceContainer steps) over shadows; in light mode, soft shadows are
acceptable.

Iconography size (`PantryIconSize`): `sm 16 · md 24 · lg 32 · xl 48`. Interactive icon
targets stay ≥ `48dp` even when the glyph is 18–24dp.

---

## 5. Components (`:core-designsystem`)

`PantryButton` (primary/secondary/destructive + `isLoading`), `PantryCard`, `PantryListItem`
(leading + trailing slots), `PantryTextField`, `PantrySearchBar`, `PantryTopBar`,
`PantryLoading`, `PantryEmptyState`, `PantryErrorState`. All consume tokens only — never
hardcoded colors.

Every screen must handle the three global states with the shared components: loading
(`PantryLoading`), empty (`PantryEmptyState`, with icon + call to action), error
(`PantryErrorState`, with a Retry button and no technical jargon).

---

## 6. Motion

Purposeful, quick, never decorative. Reference interactions:

- **Item completed (shopping mode):** checkbox fills, row fades to the "completed" style and
  animates down below the divider (`animateItemPlacement`). ~250ms, standard easing.
- **Search results:** fade/slide in as the query updates; no layout jank.
- **Undo (delete):** row removed immediately with a snackbar + UNDO for ~4s (prefer undo to
  confirmation dialogs for reversible actions).
- **Swipe actions:** swipe-left = delete (danger background), swipe-right = favorite (gold
  background), matching the existing gesture logic; always with a visible fallback control.

Durations: micro 100ms, standard 250ms, entrance 300ms. Avoid anything slower than ~350ms.

---

## 7. Accessibility & internationalization

Minimum touch target `48×48dp`. Contrast follows M3 on the semantic roles above; status and
category colors always pair with an icon or label (never color alone). Provide
`contentDescription` for meaningful icons and `null` for decorative ones. All text comes from
`strings.xml` (English + Spanish); layouts must tolerate longer translations. Support dynamic
font scaling and clear focus states.

---

## 8. Language rule

All code, comments and documentation in this repository are written in **English**.
