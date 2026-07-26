# PantryHub Design System

## Overview

This document defines the visual language and reusable design rules for PantryHub.

The purpose of the design system is to ensure that every screen, component and interaction feels like part of the same application.

The design system is based on:

- Material 3.
- Jetpack Compose.
- Accessibility principles.
- Modern Android design guidelines.
- PantryHub product philosophy.

---

# Design Goals

The PantryHub interface should communicate:

## Simplicity

The user should immediately understand what to do.

---

## Trust

Shopping data is personal and important.

The interface should feel reliable and organized.

---

## Speed

Frequent actions should be visually prioritized.

---

## Calm

The application should avoid visual overload.

Shopping is already a repetitive task. The UI should reduce cognitive effort.

---

# Design Foundation

## Platform

PantryHub uses:

- Jetpack Compose.
- Material 3.
- Material You principles.

---

## Theme Support

The application must support:

- Light theme.
- Dark theme.
- Dynamic colors when available.

Theme selection:

```
System default
Light
Dark
```

---

# Color System

Colors should communicate meaning, not decoration.

---

## Primary Color

Used for:

- Main actions.
- Active states.
- Important buttons.
- Selected elements.

Examples:

```
Create list
Start shopping
Save changes
```

---

## Secondary Color

Used for:

- Supporting actions.
- Filters.
- Categories.
- Additional information.

---

## Error Color

Used for:

- Destructive actions.
- Validation errors.
- Failed operations.

Examples:

```
Delete product
Remove list
Import failed
```

---

## Success Color

Used for:

- Completed purchases.
- Successful imports.
- Completed actions.

Example:

```
Product purchased
```

---

# Color Usage Rules

Avoid using color as the only communication method.

Bad:

```
Green = purchased
Red = pending
```

Good:

```
Icon + text + visual state
```

---

# Typography

Typography should prioritize readability.

---

## Font

Default:

```
Material Typography
```

Future customization may introduce a custom brand font.

---

## Text Hierarchy

Recommended levels:

## Display

Large promotional or empty-state content.

---

## Headline

Screen titles.

Example:

```
Weekly Shopping
```

---

## Title

Cards and sections.

Example:

```
Favorites
```

---

## Body

Normal information.

Example:

```
Milk
2 units
```

---

## Label

Small supporting information.

Example:

```
Purchased yesterday
```

---

# Spacing System

All layouts should use consistent spacing.

Base unit:

```
4dp
```

Recommended values:

```
4dp
8dp
12dp
16dp
24dp
32dp
48dp
```

Avoid arbitrary spacing.

---

# Layout Principles

## Content First

The interface should prioritize user content.

Avoid unnecessary decoration.

---

## Comfortable Touch Areas

Interactive elements should have enough space.

Minimum recommended touch target:

```
48dp
```

---

## Edge Padding

Standard screen padding:

```
16dp
```

---

# Shape System

PantryHub uses rounded surfaces.

Recommended shapes:

---

## Small

Used for:

- Chips.
- Small controls.

```
8dp
```

---

## Medium

Used for:

- Cards.
- List items.

```
12dp
```

---

## Large

Used for:

- Dialogs.
- Important containers.

```
24dp
```

---

# Component System

Reusable components should live in:

```
ui/components
```

---

# Buttons

## Primary Button

Used for main actions.

Examples:

```
Create List
Start Shopping
Save
```

---

## Secondary Button

Used for alternative actions.

Examples:

```
Cancel
Edit
```

---

## Text Button

Used for low priority actions.

Examples:

```
See more
Clear
```

---

# Floating Action Button

The FAB should represent the primary action of a screen.

Examples:

Lists screen:

```
+
Create list
```

Products screen:

```
+
Create product
```

Avoid multiple FABs.

---

# Cards

Cards should contain related information.

Examples:

Shopping list card:

```
Weekly Shopping

24 products

Last updated today
```

Cards should not become containers for everything.

---

# List Items

Lists are a core element of PantryHub.

List items should support:

- Fast scanning.
- Quick actions.
- Clear states.

---

## Product List Item

Example:

```
Milk

Dairy

          ✓   ✎
```

Actions:

- Complete.
- Edit.

---

## Shopping Item States

Pending:

```
Milk
```

Completed:

```
✓ Milk
```

The completed state should remain readable.

---

# Swipe Actions

Swipe gestures may be used.

Examples:

Swipe right:

```
Complete
```

Swipe left:

```
More actions
```

Every gesture must have a visible alternative.

---

# Search Component

Search is a primary interaction in PantryHub.

Requirements:

- Always easy to access.
- Immediate filtering.
- Support partial input.
- Support categories.

---

## Search Result Priority

Order:

1. Exact matches.
2. Favorites.
3. Frequently purchased.
4. Alphabetical order.

---

# Empty States

Empty states must guide users.

Structure:

```
Illustration

Title

Explanation

Action
```

Example:

```
No shopping lists yet

Create your first list to start organizing purchases.

Create list
```

---

# Loading States

Avoid blocking screens.

Preferred:

- Skeleton loading.
- Inline progress.
- Immediate optimistic updates.

---

# Dialogs

Dialogs should be used only for important decisions.

Use dialogs for:

- Data deletion.
- Import conflicts.
- Duplicate resolution.

Avoid dialogs for normal actions.

---

# Notifications and Feedback

Preferred feedback:

- Snack-bar.
- Inline message.
- Visual state change.

Examples:

```
Product added

List exported successfully

Changes saved
```

---

# Icons

Icons should be:

- Simple.
- Recognizable.
- Consistent.

Use:

```
Material Icons
```

Avoid mixing unrelated icon styles.

---

# Animation System

Animations should explain changes.

Recommended:

- Item movement.
- State transitions.
- Screen navigation.

Avoid:

- Decorative animations.
- Long transitions.

---

# Motion Principles

Animations should be:

- Fast.
- Natural.
- Predictable.

Recommended duration:

```
150ms - 300ms
```

---

# Accessibility

The design system must support:

- Screen readers.
- Large text.
- High contrast.
- Keyboard navigation.
- Minimum touch targets.

---

# Responsive Design

The application should support:

- Different phone sizes.
- Tablets when possible.
- Different orientations.

Layouts should not depend on fixed dimensions.

---

# Localization

The design system must support translations.

Requirements:

- No hardcoded UI text.
- Flexible layouts.
- Longer translations.
- Right-to-left preparation.

---

# Dark Theme Principles

Dark theme should preserve hierarchy.

Rules:

- Avoid pure black everywhere.
- Maintain readable contrast.
- Preserve component elevation.
- Adjust colors instead of simply inverting.

---

# Component Naming Convention

Compose components should follow:

```
PantryHub + ComponentName
```

Examples:

```
PantryHubButton

PantryHubSearchBar

PantryHubListItem
```

---

# UI Architecture Rules

UI components should:

- Be reusable.
- Have minimal logic.
- Receive state.
- Emit events.

Business logic belongs outside UI components.

---

# Final Design Principle

Every visual decision should answer:

"Does this make shopping faster, clearer or easier?"

If not, it should not exist.

---
Last updated: July 26, 2026
