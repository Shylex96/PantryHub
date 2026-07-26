# PantryHub User Flows

## Overview

This document defines the main user journeys inside PantryHub.

User flows describe how users interact with the application to achieve specific goals.

They are focused on user objectives rather than technical implementation.

---

# User Types

## Personal User

A person using PantryHub individually.

Main goals:

- Create shopping lists.
- Remember products.
- Complete purchases.
- Maintain personal shopping history.

---

## Household User

A person using PantryHub as part of a shared household.

Future capabilities:

- Shared lists.
- Shared products.
- Shared notes.
- Collaborative purchases.

---

# Core User Flow Principles

All user flows should follow these principles:

- Minimum number of steps.
- Clear feedback after actions.
- Avoid unnecessary confirmations.
- Preserve user control.
- Support offline usage.
- Prioritize frequent actions.

---

# Flow 1 - First Application Launch

## Objective

Allow a new user to start using PantryHub quickly.

---

## Flow

```
Open application

↓

Application initialization

↓

Load local configuration

↓

Show main experience

↓

User can create first list
```

---

## Requirements

The first launch should not require:

- Account creation.
- Internet connection.
- Complex configuration.

Optional configuration:

- Language.
- Theme preference.
- Initial categories.

---

# Flow 2 - Create Shopping List

## Objective

Create a list for a future purchase.

---

## Starting Points

User can create a list from:

- Empty list.
- Existing template.
- Existing previous list.

---

## Empty List Flow

```
User selects create list

↓

Enter list name

↓

List is created

↓

User adds products
```

---

## Template Flow

```
User selects create from existing

↓

Select previous list

↓

Create copy

↓

Modify products

↓

Save new list
```

---

## Requirements

A new list should not affect the original list.

Example:

Original:

```
Weekly Shopping
```

New:

```
Holiday Shopping
```

Changes must remain independent.

---

# Flow 3 - Add Product To List

## Objective

Quickly add an item to a shopping list.

---

## Preferred Flow

```
Open list

↓

Tap add product

↓

Search product

↓

Select existing product

↓

Product added
```

---

## Search Behavior

The search should support:

- Partial names.
- Alphabetical ordering.
- Favorites.
- Purchase frequency.

Example:

Input:

```
pa
```

Results:

```
Pan
Pasta
Patatas
Pavo
```

---

# Flow 4 - Create New Product

## Objective

Create a product that does not exist.

---

## Flow

```
Search product

↓

No matching product found

↓

Create product

↓

Assign category

↓

Save product

↓

Add to list
```

---

## Product Rules

Before creating:

The application should check:

- Existing names.
- Accent differences.
- Similar products.

Example:

Existing:

```
Melón
```

New:

```
Melon
```

The application should suggest using the existing product.

---

# Flow 5 - Browse Products By Category

## Objective

Help users remember products they forgot.

---

## Flow

```
Open product selector

↓

Select category

↓

Browse products

↓

Select items

↓

Add to list
```

---

## Example

User does not remember what is missing.

They open:

```
Vegetables
```

Possible results:

```
Tomatoes
Carrots
Lettuce
Onions
Peppers
```

---

# Flow 6 - Duplicate Shopping List

## Objective

Reuse an existing list as a base.

---

## Example

Existing:

```
Weekly Shopping
```

Contains:

```
50 products
```

New purchase requires:

```
40 products
```

Instead of creating a new list:

```
Duplicate list

↓

Remove unnecessary items

↓

Rename list

↓

Start shopping
```

---

## Rules

Duplication creates a new independent entity.

Changes do not affect the original list.

---

# Flow 7 - Start Shopping Mode

## Objective

Use a list while shopping in a supermarket.

---

## Flow

```
Open shopping list

↓

Start shopping mode

↓

View pending products

↓

Mark products as purchased

↓

Finish purchase
```

---

## Shopping Mode Behavior

Before:

```
Pending:

Milk
Bread
Eggs
Rice
```

After completing Bread:

```
Pending:

Milk
Eggs
Rice


Completed:

Bread
```

---

## Requirements

Shopping mode should provide:

- Large touch targets.
- Fast interactions.
- Clear visual separation.
- Minimal distractions.

---

# Flow 8 - Complete Purchase

## Objective

Finish a shopping session.

---

## Basic Completion

```
Finish shopping

↓

Confirm completion

↓

Return to list
```

No purchase information is stored.

---

## Detailed Completion

```
Finish shopping

↓

Save purchase information

↓

Enter total price

↓

Select supermarket

↓

Confirm
```

Stored information:

- Date.
- Products.
- Price.
- Supermarket.

---

## Supermarket Validation

If the user confirms:

```
All products were purchased in the same supermarket
```

The supermarket can be associated with the purchase.

Otherwise:

The purchase should not assign a single supermarket.

---

# Flow 9 - Export Data

## Objective

Allow users to back up or share information.

---

## Flow

```
Open settings

↓

Export data

↓

Generate JSON file

↓

Share or save file
```

---

## Export Should Include

Possible data:

- Products.
- Categories.
- Lists.
- Templates.
- Notes.
- Purchase history.

---

# Flow 10 - Import Data

## Objective

Restore or receive PantryHub information.

---

## Flow

```
Open import

↓

Select JSON file

↓

Validate structure

↓

Detect conflicts

↓

Confirm import

↓

Merge data
```

---

## Conflict Detection

Possible conflicts:

Same:

```
Arroz
arroz
Arroz
```

Should merge.

Possible conflicts:

```
Lenteja
Lentejas
```

Requires user decision.

---

# Flow 11 - Future Household Creation

## Objective

Create a shared shopping environment.

---

## Flow

```
Create household

↓

Generate invitation

↓

Share QR or link

↓

Members join

↓

Shared information available
```

---

## Shared Data

Possible shared elements:

- Lists.
- Products.
- Categories.
- Notes.
- Purchases.

---

# Flow 12 - Future Synchronization

## Objective

Keep household members updated.

---

## Flow

```
User changes data

↓

Local update

↓

Synchronization service

↓

Other members receive update
```

---

## Requirements

Synchronization must support:

- Offline changes.
- Conflict resolution.
- Data consistency.
- User permissions.

---

# Flow 13 - Notes

## Objective

Store useful information related to shopping.

---

## Personal Notes

Example:

```
Tomatoes are cheaper on Fridays.
```

---

## Household Notes

Future example:

```
Remember buying batteries.
```

---

# Future User Flow Considerations

Potential future journeys:

- Automatic list generation.
- Purchase predictions.
- Budget management.
- Price tracking.
- Smart recommendations.
- Consumption analysis.

---

# Final User Experience Goal

Every user flow should move PantryHub towards this objective:

```
Remember less.

Organize faster.

Shop easier.
```

---
Last updated: July 26, 2026
