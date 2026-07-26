# PantryHub Design System

## Overview

This is the central reference for the visual language and reusable design rules of PantryHub. All UI development must adhere to these guidelines to ensure consistency, accessibility, and a modern user experience.

The design system is built upon **Material 3** and **Jetpack Compose**, leveraging the latest Android design principles.

---

# 1. Visual Identity

PantryHub aims to feel **organized, trustworthy, and calm**. The interface uses soft tones inspired by home and nature (Sage, Earth, Terracotta) to reduce the mental friction of daily chores.

---

# 2. Color System

We use a semantic color system that adapts to both Light and Dark modes.

### Brand Palette
- **Primary (Sage Green)**: Represents growth and organization. Used for main actions.
- **Secondary (Earthy Neutral)**: Represents home and stability. Used for supporting elements.
- **Tertiary (Soft Terracotta)**: Adds warmth and human touch.

### Semantic Colors
- **Success**: Positive actions, completed items.
- **Warning**: Actions requiring attention.
- **Error**: Destructive actions, failures.
- **Info**: Neutral information.

---

# 3. Spacing System (PantrySpacing)

Never use arbitrary values. All gaps and paddings must use the `PantrySpacing` tokens:

| Token | Value | Usage |
|-------|-------|-------|
| `xs`  | 4dp   | Micro-spacing, tight grouping |
| `sm`  | 8dp   | Internal component padding |
| `md`  | 12dp  | Content grouping |
| `lg`  | 16dp  | Screen edges, main item padding |
| `xl`  | 24dp  | Section separation |
| `xxl` | 32dp  | Hero areas, very large gaps |

Access via: `PantryHubTheme.spacing.md`

---

# 4. Typography

Hierarchy optimized for mobile reading and fast scanning in the supermarket:

- **Display**: Large numbers or hero headlines.
- **Headline**: Screen titles.
- **Title**: Card and list item headers.
- **Body**: Primary content (product names, notes).
- **Label**: Secondary details (quantities, dates).

---

# 5. Core Components (`:core-designsystem`)

### PantryButton
The main interactive element.
- **Types**: `Primary`, `Secondary`, `Destructive`.
- **States**: Default, Disabled, Loading (built-in spinner).

### PantryTopBar
Standardized header. Always includes the title and supports navigation icons and actions.

### PantrySearchBar
Integrated search functionality with consistent icons and styling.

### PantryListItem
The building block for all lists (Products, Lists, Notes). Supports leading icons, titles, subtitles, and trailing actions.

---

# 6. Screen State Management

Every screen should handle these three states using standardized components:
1. **PantryLoading**: Centered progress indicator.
2. **PantryEmptyState**: Descriptive icon + text + optional action.
3. **PantryErrorState**: Error message + retry action.

---

# 7. Design Rules & Best Practices

1. **Touch Targets**: Minimum **48x48dp** for all interactive elements.
2. **No Hardcoded Strings**: All text must come from `strings.xml`.
3. **Reactive UI**: Components must be stateless (receive state, emit events).
4. **Consistency**: If a component doesn't exist in `core-designsystem`, create it there before using it in a feature.
5. **Mode Support**: Test every screen in both Light and Dark modes.

---
Last updated: July 26, 2026
