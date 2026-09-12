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

Shapes (`PantryShapes`): small `8dp`, medium `16dp`, large `24dp`, plus two composition
shapes added by the redesign: **card `20dp`** and **sheet `28dp`** (top corners of bottom
sheets). Rows, fields and buttons use medium (16); list cards and settings groups use card
(20); pills use full (999).

Spacing (`PantrySpacing`): `xs 4 · sm 8 · md 12 · lg 16 · xl 24 · xxl 32` plus **`screen
20dp`** (horizontal screen margin, added by the redesign). Rows are separated by `sm`, cards
by `md`, category groups by `18dp`, sections from the header by `28dp`.

Elevation (`PantryElevation`): `low 2 · medium 4 · high 8`. In dark mode, prefer **tonal
elevation** (lighter surfaceContainer steps) over shadows; in light mode, soft shadows are
acceptable.

Iconography size (`PantryIconSize`): `sm 16 · md 24 · lg 32 · xl 48`. Interactive icon
targets stay ≥ `48dp` even when the glyph is 18–24dp.

---

## 5. Components (`:core-designsystem`)

`PantryButton` (primary/secondary/destructive + `isLoading`), `PantryCard`, `PantryItemCard`
(filled row), `PantryListItem` (leading + trailing slots), `PantryBadge` (pill),
`PantryCheckbox` (rounded), `PantryDialog`, `PantryTextField` (label or placeholder mode),
`PantrySearchBar`, `PantryTopBar` (raised, bold title, optional subtitle), `PantryLoading`,
`PantryEmptyState`, `PantryErrorState`. All consume tokens only — never hardcoded colors.

The redesign (§6) adds the composition components, all in place since R2:
`PantryScreenHeader` (title + action + subtitle) and `PantryHeaderIconButton`,
`PantrySectionLabel` (uppercase label, optional dot or icon, count), `PantryListCard` (icon
tile + meta + optional progress) and `PantryProgressBar`, `PantryExtendedFab`,
`PantryBottomCta` (gradient fade + count pill), `PantrySheet` (bottom sheet container with
title action slot, subtitle and scrollable content) with `PantryFieldLabel`,
`PantryChoiceCard` (2-column choice), `PantryOptionRow` (single/multi select row with dot
and count, grows to two lines) and `PantrySegmentedRow` (equal pills, one selected),
`PantrySearchField` (48dp pill), `PantryItemCard` with `onLongClick` for contextual
actions, `PantrySectionLabel` with an optional trailing `action`, `PantrySwipeRow` (the app-wide swipe-left-delete / swipe-right-favorite
treatment, with `dismissOnDelete = false` when a confirmation follows). `PantryTopBar` is
no longer used by any screen and is scheduled for removal. `PantryDialog` is kept only for
destructive confirmations.

Every screen must handle the three global states with the shared components: loading
(`PantryLoading`), empty (`PantryEmptyState`, with icon + call to action), error
(`PantryErrorState`, with a Retry button and no technical jargon).

---

## 6. Screen composition (approved redesign, 2026-09)

The redesign approved on 2026-09-11 (canvas "PantryHub Redesign") defines how every screen
is composed. The values below are the source of truth for implementation; components in
`:core-designsystem` must expose them, never re-invent them per screen.

### 6.1 Tab screen anatomy

Every one of the four tabs (Lists, Products, Notes, Settings) opens the same way:

| Element | Spec |
|---|---|
| Header title | Space Grotesk 700 · 32/38 · letter-spacing −0.4 · `onSurface` · top padding 60 (below the system status bar) |
| Header action | 40×40 icon button · radius 12 · `surfaceContainer` · glyph 20 `onSurfaceVariant` (search, filter…) |
| Subtitle | Inter 14/20 · `onSurfaceVariant` · live counts ("3 lists · 11 pending products") · 6 below the title |
| Section label | Inter 600 · 11/16 · letter-spacing 1.2 · UPPERCASE · `onSurfaceVariant`; the first/emphasised section may use `primary`; the Favorites group uses `extendedColors.favorite` · right-aligned count in 12 `outline` · 28 below the header, 10 above its content |
| Screen margin | 20 (`spacing.screen`) on both sides |
| Bottom padding | content clears the FAB (104 + 56) or the bottom CTA (140) |

### 6.2 List card (Lists tab)

`surfaceContainer` · radius 20 · padding 16 · column gap 14.
Row: icon tile 46×46 radius 14 in a tinted container (`primaryContainer`,
`tertiaryContainer`, `secondaryContainer` or `surfaceContainerHighest`) with a 22 glyph ·
gap 14 · title Inter 600 16/22 · meta Inter 13/18 `onSurfaceVariant` · trailing chevron 20
in `outline`.
Type pill (one-off): Inter 600 11/16 · padding 1×8 · radius 999 · `tertiaryContainer` /
`onTertiaryContainer`, inline after the title.

**Shopping-in-progress rule.** A list is *in progress* when at least one of its items is
checked (`items.any { isCompleted }`), i.e. the user has started ticking things off in
shopping mode. Nothing about the number of products makes a list "pending" — a list you
have not started shopping is simply a list. The rule drives three things:

- **Lists tab sections.** In-progress lists appear first under the accent label
  "Shopping in progress"; every other list sits under "All lists" (that second label is
  shown only when the first section exists). Finishing a shop resets (regular) or deletes
  (one-off) the list, so it drops out of "Shopping in progress" by itself.
- **Card meta.** Empty list → "No products yet" · in progress → "3 of 8 in the cart" ·
  otherwise → "8 products". The 4dp progress bar (track `surfaceContainerHighest`, fill
  `primary`) renders **only** while in progress; never on an untouched list.
- **List detail.** Same meta line; the "3 / 8" counter and progress bar appear only while
  in progress. The bottom call-to-action reads **"Start shopping"** (count = all items) on
  an untouched list and **"Continue shopping"** (count = items still to pick) while in
  progress — leaving shopping mode never loses the ticks. The header subtitle of the Lists
  tab counts lists and products, never "pending".

### 6.3 Item row (detail, shopping mode, Products)

`surfaceContainerHigh` · radius 16 · height 60 (64 in shopping mode) · padding 0 8 0 16 ·
gap 12. Category dot 10 (8 in group headers; uncategorised = `onSurfaceVariant` at 30%) ·
name Inter 500 15 `onSurface` · optional second line Inter 12 `onSurfaceVariant` ·
trailing 40×40 icon buttons with 20 glyphs. **Rows show only the favorite star; delete is
swipe-left.** Quantity/unit renders as trailing Inter 13 `onSurfaceVariant` text.
Completed row (shopping mode): `surfaceContainerLow` · height 56 · text `onSurfaceVariant`
with line-through · dot at 50% opacity.
Checkbox (`PantryCheckbox`): 26×26 · radius 8 · unchecked 2dp `outline` border · checked
`primary` fill with `onPrimary` check.

### 6.4 Inputs and primary actions

| Element | Spec |
|---|---|
| Search field | height 48 · radius 999 · `surfaceContainer` + 1dp `outlineVariant` border · leading 20 glyph · placeholder Inter 15 `onSurfaceVariant` |
| Add bar | field height 52 · radius 16 · leading "+" glyph · placeholder, **plus** a 52×52 solid `primary` square (radius 16) — always the same height, vertically centred |
| Extended FAB | height 56 · radius 16 · `primary`/`onPrimary` · 22 glyph + Inter 600 15 label ("New list") · shadow 0 10 24 primary@28% · anchored right 20, bottom 104 (above the nav) |
| Bottom CTA (secondary screens) | full width inside the 20 margin · height 56 · radius 16 · `primary` · label Inter 600 16 · optional count pill (`onPrimary` bg, `onPrimaryContainer` text) · sits over a 140dp gradient fade to `background` · bottom 32 |
| Top-bar icon button | 40×40 · radius 12 · `surfaceContainer` · glyph 20–22 |

### 6.5 Bottom sheets replace dialogs

All creation/editing flows are bottom sheets, never `AlertDialog`: new/rename list, new/edit
product, category manager, product filter, finish shopping, note editor, theme, language
and Settings › Manage data.
Container `surfaceContainer` · top radius 28 · grabber 40×4 `outline` centred · padding
12 20 36 · section gap 20–22 · scrim `surfaceContainerLowest` at 55%.
Title Space Grotesk 700 24/30 + Inter 14 subtitle. Field labels Inter 600 12 UPPERCASE
letter-spacing 0.6 `onSurfaceVariant`. Fields height 56 · radius 16 · `surfaceContainerHigh`
· 1.5dp border `outlineVariant`, `primary` when focused. Choice cards (e.g. list type) in a
2-column grid, radius 16, title + one-line explanation; selected = `primaryContainer` + 1.5dp
`primary` border + check glyph. Primary button full width, height 56.

### 6.6 Navigation

- Bottom navigation only on the four tabs: height 84 · `surfaceContainer` · 1dp top
  `outlineVariant` · 24 glyph + Inter 12 label · selected `primary` (600), unselected
  `onSurfaceVariant` (500) · **no indicator pill behind the icon**.
- Secondary screens (list detail, shopping mode, import/export, help) **hide the bottom
  navigation** and use a top bar with a 40×40 back chevron ("<"), optional actions, and a
  bottom CTA when the screen has one primary action.
- No hamburger / drawer menu.

### 6.7 Grouping and filtering (no horizontal scrollers)

- List detail, shopping mode and the Products tab group rows **by category**. Group header =
  dot 8 + UPPERCASE label + count. In Products, a **Favorites** group comes first (gold
  label) and **No category** last (grey dot).
- **Horizontal chip scrollers are not used anywhere.** Filtering in Products is the header
  filter icon button → a bottom sheet listing "All categories" (selected by default) and
  every category with its dot and product count, then "No category", plus a 3-option sort
  (Category · A–Z · Most used) and a "Show N products" primary button with a "Reset" link.
- Shopping mode header: centred UPPERCASE "Shopping mode" label in the top bar · list name
  Space Grotesk 700 26/32 · big counter Space Grotesk 700 40 `primary` followed by "of 8 in
  the cart" Inter 16 `onSurfaceVariant` and the percentage · 6dp progress bar.

---

## 7. Motion

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

## 8. Accessibility & internationalization

Minimum touch target `48×48dp`. Contrast follows M3 on the semantic roles above; status and
category colors always pair with an icon or label (never color alone). Provide
`contentDescription` for meaningful icons and `null` for decorative ones. All text comes from
`strings.xml` (English is the source; Spanish today, more languages planned — see
`04_UX_Guidelines.md` "Localization" for the default-to-system rule and the add-a-language
checklist); layouts must tolerate longer translations. Support dynamic font scaling and clear
focus states.

---

## 9. Language rule

All code, comments and documentation in this repository are written in **English**.
