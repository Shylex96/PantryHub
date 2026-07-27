# PantryHub Design System Foundation

## Overview
The PantryHub Design System is the single source of truth for the application's visual language. It provides a set of tokens and reusable components to ensure consistency, accessibility, and high development velocity.

---

# 1. Visual Tokens

Visual tokens are the smallest atoms of our design system. Never use hardcoded values in UI modules.

### 1.1 Spacing (PantrySpacing)
Standardized gaps between elements.
- `xs`: 4dp (Tight grouping)
- `sm`: 8dp (Inner component padding)
- `md`: 12dp (Content grouping)
- `lg`: 16dp (Screen edges)
- `xl`: 24dp (Section separation)

### 1.2 Elevations
Control depth and hierarchy.
- `low`: 2dp (Default cards)
- `medium`: 4dp (Elevated items)
- `high`: 8dp (Dialogs/Modals)

### 1.3 Iconography (PantryIcons)
Semantic mapping for common actions.
- `PantryIcons.Add`, `Delete`, `Edit`, `Search`.
- Navigation: `Lists`, `Products`, `Notes`, `Settings`.

---

# 2. Reusable Components

All components are located in the `:core-designsystem` module.

### 2.1 PantryButton
The primary interaction element.
- **Usage**: Use `Primary` for the main action, `Secondary` for alternatives, and `Destructive` for data deletion.
- **States**: Supports a built-in `isLoading` state which replaces the text with a spinner.

### 2.2 PantryListItem
The foundational block for all lists.
- **Usage**: Mandatory for Shopping Lists, Product Catalogs, and Notes.
- **Structure**: Supports leading icons/checks and trailing actions (e.g., Delete/Edit).

### 2.3 PantryCard
Standardized container for grouped information.
- **Usage**: High-level summaries or dashboard-style items.

---

# 3. Global UI States

Every screen in PantryHub must handle the following states using the standardized components:

| State | Component | Requirement |
|-------|-----------|-------------|
| **Loading** | `PantryLoading` | Use for initial data fetch. |
| **Empty** | `PantryEmptyState` | Provide a clear icon and a call to action. |
| **Error** | `PantryErrorState` | Explain what happened and provide a **Retry** button. |

---

# 4. Accessibility Rules
1. **Minimum Touch Target**: Every interactive element must be at least **48x48dp**.
2. **Contrast**: Use semantic colors from `MaterialTheme.colorScheme` to ensure M3 contrast standards.
3. **Screen Readers**: Always provide `contentDescription` for icons or use `null` for decorative elements.

---
Last updated: July 27, 2026
