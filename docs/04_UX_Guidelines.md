# PantryHub UX Guidelines

## Overview

This document defines the user experience principles and interaction guidelines for PantryHub.

The objective is to create an application that feels fast, natural and reliable while reducing the effort required to organize and complete shopping tasks.

These guidelines define how the application should behave before defining specific visual components.

---

# UX Vision

PantryHub should feel like:

- A personal shopping assistant.
- A memory extension for the user.
- A tool that becomes more useful over time.

The application should avoid feeling like a complex management system.

The user should always feel that PantryHub helps rather than requires administration.

---

# Core UX Principles

## 1. Fast Actions First

The most common actions should always be the easiest.

The application should optimize:

- Adding products.
- Completing purchases.
- Reusing lists.
- Searching items.

Rare actions can require additional steps.

---

## 2. Progressive Complexity

The user should not see unnecessary complexity.

Example:

A new user should immediately understand:

```
Create list
Add products
Go shopping
```

Advanced features should appear naturally:

```
History
Analytics
Synchronization
Household
```

when they become relevant.

---

## 3. Predictable Behavior

Users should always understand:

- What happened.
- Why it happened.
- What they can do next.

Avoid unexpected actions.

---

## 4. Immediate Feedback

Every important action should provide feedback.

Examples:

Adding product:

```
Product added
```

Completing item:

```
Item moved to completed section
```

Deleting:

```
Product removed
Undo available
```

---

# Navigation Principles

## Main Navigation

The application should prioritize the most common areas.

Main sections — a **bottom navigation bar with four tabs**, each with an icon and a text
label (the Lists tab is the home screen; there is no separate Home tab and no hamburger /
drawer menu):

```
Lists · Products · Notes · Settings
```

Future sections:

```
Household
Reports
History
```

---

## Navigation Rules

Navigation should:

- Require minimal steps.
- Preserve user context.
- Avoid unnecessary screens.

Example:

Opening a list should remember:

- Scroll position.
- Filters.
- Shopping mode state.

Structural rules (redesign 2026-09, see `05_Design_System.md` §6):

- Every tab opens with a **large title, a context subtitle with live counts and one
  optional header action** (search or filter). The primary creation action is an
  **extended FAB with a text label** ("New list", "New product", "New note").
- **Secondary screens hide the bottom navigation**: list detail, shopping mode,
  import/export and help use a top bar with a back chevron ("<") and, when the screen has
  one primary action, a full-width **bottom call-to-action** ("Start shopping · 8", "Finish
  shopping").
- The selected tab is shown by tinting icon and label in the accent color — no indicator
  pill behind the icon.
- **Creation and editing happen in bottom sheets, not dialogs** (new/edit list, new/edit
  product, categories, product filter, finish shopping). Dialogs remain only for
  destructive confirmations ("Delete list?").
- **Delete is a swipe-left gesture**; rows do not show a trash icon. A snackbar with Undo
  follows every delete.

---

# Home Experience

## Objective

Provide quick access to current activity.

Possible content:

- Active shopping lists.
- Recent lists.
- Favorites.
- Suggested products.

The home screen should not become an information dashboard.

Its purpose is action.

---

# List Experience

## Shopping Lists

Lists should prioritize:

- Clarity.
- Quick scanning.
- Fast editing.

---

## List Items

Each item should support:

- Tap to interact.
- Long press for additional actions.
- Quick actions.

---

## Gmail Style Interaction

The application should support efficient inline actions.

Example:

```
Milk          ✓   ✎   ×
Bread         ✓   ✎   ×
Eggs          ✓   ✎   ×
```

Possible actions:

- Complete.
- Edit.
- Remove.

---

## Gestures

Gestures may include:

Swipe actions:

```
Swipe right:
Complete item

Swipe left:
More actions
```

Long press:

```
Selection mode
Bulk actions
```

Gestures should always have visible alternatives.

---

# Product Adding Experience

## Objective

Adding products should be one of the fastest actions.

Preferred interaction:

```
Open search

↓

Type

↓

Select product

↓

Automatically added
```

---

## Search Behavior

Search should:

- Start immediately.
- Support partial text.
- Ignore capitalization.
- Handle accents.
- Prioritize relevant products.

Example:

Input:

```
melon
```

Results:

```
Melón
```

---

# Product Discovery

## Objective

Help users remember forgotten products.

The application should support discovery through:

- Categories.
- Favorites.
- Previous purchases.
- Frequently used products.

---

## Category Browsing

Categories should feel like exploration — and they must never hide behind a horizontal
scroller.

Rules (redesign 2026-09, see `05_Design_System.md` §6.7):

- The Products tab **groups products by category**. Each group has a header with the
  category's color dot, its name in small uppercase and its product count. The
  **Favorites** group comes first; **No category** comes last with a grey dot.
- **No horizontal chip rows.** They hide categories off-screen and do not scale. Filtering
  is a single filter icon button in the header that opens a **bottom sheet** listing "All
  categories" (default) and every category with dot + count, plus "No category", a
  three-way sort (Category · A–Z · Most used) and a primary "Show N products" button with a
  "Reset" link.
- Search stays a permanent pill field under the header; it filters within the current
  category selection.
- The same grouping is used inside a list's detail and in shopping mode, so a category
  reads the same everywhere.

Example — the Vegetables group in Products:

```
● VEGETABLES                                   4
  Tomatoes
  Carrots
  Lettuce
  Peppers
```

---

# Shopping Mode UX

## Objective

Create a supermarket-friendly experience.

Shopping mode is one of the most important experiences in PantryHub.

---

## Requirements

The interface should prioritize:

- Large touch areas.
- Minimal typing.
- Clear hierarchy.
- One-handed usage.
- Fast completion.

---

## Item Completion

When completing an item:

Before:

```
Milk
Bread
Eggs
```

After:

```
Pending:

Milk
Eggs


Completed:

Bread
```

The movement should be visually clear.

---

## Shopping Mode Restrictions

Avoid:

- Complex editing.
- Too many buttons.
- Distracting information.

The user is currently buying.

---

# Notes UX

## Objective

Notes hold whatever does not fit a list: a recipe, a price to remember, a reminder.

- The Notes tab is a **two-column grid of cards**: title (or "Untitled"), an excerpt of up
  to five lines and a relative "last edited" date. Tapping a card opens the editor.
- The header carries a **search toggle**; search filters by title and content and shows the
  standard "no matches" empty state.
- The **editor is a bottom sheet** (title + multi-line content). Save is enabled as soon as
  either field has text. An existing note can be deleted from the editor, after a
  confirmation dialog — deletion is the only destructive action on the screen, so no swipe
  gesture is used on the grid.

---

# Settings UX

## Objective

Settings are rare visits; they must be scannable and safe.

- Sections (`Appearance`, `Language`, `Data`, `Help`) are uppercase labels above **one card
  each**, with 60dp rows: tinted icon tile · title (+ one-line description) · current value ·
  chevron. A row with a toggle shows the switch instead of a chevron.
- **Choices open bottom sheets** (theme, language) listing the options as selectable rows;
  picking one applies immediately and closes the sheet.
- **Data › Backup** opens the import/export screen; **Data › Manage data** opens the
  bulk-delete sheet.

## Manage data (start over)

The Manage data sheet lets the user wipe any combination of: all shopping lists · all
products (this also empties every list) · all categories (products are kept and become
uncategorised) · all notes · purchase history · unmark all favorites — or **Everything
(start over)**, which selects them all. The button "Delete selected" is red, disabled until
something is selected, and always followed by a **confirmation dialog that names exactly
what will be removed**. Theme and language settings are never touched. When the wipe
completes the sheet closes and a snackbar confirms it. The sheet subtitle points the user to
the backup screen first.

---

# Empty States

Empty states should guide users. Use `PantryEmptyState` from the Design System to maintain visual consistency and provide helpful next steps.

---

# Error Handling

Errors should be understandable and actionable. Use `PantryErrorState` to provide a "Retry" mechanism and avoid technical jargon.

Bad:

```
Database constraint violation
```

Good:

```
This product already exists.
Would you like to use the existing one?
```

---

# Confirmation Rules

Avoid unnecessary confirmations.

Do not ask:

```
Are you sure?
```

for harmless actions.

---

Use confirmation for:

- Permanent deletion.
- Data overwrite.
- Import conflicts.
- Destructive merges.

---

# Undo Pattern

Prefer undo to confirmation.

Example:

Delete product:

```
Product deleted

UNDO
```

This keeps interaction fast.

---

# Forms

Forms should request only necessary information.

Example:

Creating a product:

Required:

```
Name
```

Optional:

```
Category
Notes
Favorite
```

---

# Dark Mode

Dark mode should not simply invert colors.

Requirements:

- Maintain readability.
- Preserve hierarchy.
- Avoid excessive contrast.
- Support OLED-friendly usage when possible.

---

# Localization

The UI must support multiple languages.

Requirements:

- No hardcoded text.
- Flexible layouts.
- Avoid text assumptions.
- Support longer translations.

Rules (2026-09):

- **The default language is the system language.** A fresh install never forces a
  language: the per-app locale list is empty until the user picks one in Settings ›
  Language (or in Android 13+ system settings, which the app advertises through
  `locales_config.xml`). "System default" is always the first option.
- English (`values/`) is the source of truth; every other language is a full translation in
  `values-<tag>/strings.xml` inside `:core-designsystem`. Strings that must not be
  translated (URLs, identifiers) carry `translatable="false"`.
- The language picker is generated from one list — `AppLanguages.supported` in
  `:feature-settings` — and labels each language by its own name ("Español", "English"),
  so adding a language never needs new picker strings.
- **Adding a language** = four edits, kept in sync: `values-<tag>/strings.xml` (all keys,
  plurals with the right quantity forms), the tag in `AppLanguages.supported`, in
  `app/src/main/res/xml/locales_config.xml`, and in `androidResources.localeFilters`
  (`app/build.gradle.kts`). Then check every screen with the longest translation.
- Counts always use `<plurals>`, dates use system formatting (`DateUtils`), and no string
  is assembled from fragments — grammar differs between languages.

---

# Accessibility

Accessibility is a requirement.

The application should support:

- Screen readers.
- Dynamic font sizes.
- Sufficient contrast.
- Clear focus states.
- Large touch targets.

---

# Animations

Animations should communicate changes.

Good examples:

- Item moving to completed section.
- List creation.
- Search results appearing.

Avoid:

- Decorative animations.
- Slow transitions.
- Excessive effects.

---

# Performance Perception

The application should feel instant.

Important actions should provide immediate response.

Examples:

Adding item:

Immediate UI update.

Database update:

Background operation.

---

# Privacy Experience

Users should understand when data leaves the device.

Future features:

- Synchronization.
- Sharing.
- Household collaboration.

must clearly communicate:

- What is shared.
- With whom.
- When.

---

# UX Decision Rule

When choosing between two designs:

Prefer the option that:

1. Requires fewer actions.
2. Requires less thinking.
3. Provides clearer feedback.
4. Gives the user more control.
5. Remains scalable.

---

# Final UX Goal

PantryHub should make users think:

"I don't need to remember my shopping anymore. The app remembers it for me."

---
Last updated: September 11, 2026
