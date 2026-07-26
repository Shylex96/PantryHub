# PantryHub Domain Model

## Overview

This document defines the core business entities and concepts of PantryHub.

The domain model represents the real-world concepts handled by the application.

It is independent of:

- Android.
- Room.
- Retrofit.
- JSON.
- UI implementation.

The domain layer represents what PantryHub means, not how it is stored.

---

# Domain Principles

## Reusable Information

Entities should represent reusable knowledge.

Examples:

A product:

```
Milk
```

exists independently of a shopping list.

A shopping list only references that product.

---

## Identity Over Name

Names are not unique identifiers.

Two products may have similar names.

Example:

```
Apple
Green Apple
```

may represent different products.

The system must identify entities using unique identifiers.

---

## User Control

The application should assist decisions but avoid silently changing user data.

Especially in:

- imports,
- merges,
- synchronization,
- duplicate detection.

---

# Core Domain Entities

The initial domain model contains:

```
Product

Category

ShoppingList

ShoppingItem

Purchase

PurchaseItem

Note
```

Future entities:

```
Household

User

Invitation

SynchronizationRecord
```

---

# Product

## Purpose

Represents an item that can be purchased.

Products are reusable entities.

Example:

```
Milk
Bread
Rice
Tomatoes
```

---

## Responsibilities

A Product stores:

- Name.
- Category.
- User preferences.
- Purchase behaviour.

---

## Conceptual Model

```
Product

id

name

category

favorite

usageFrequency

createdAt

updatedAt
```

---

## Rules

Product names should be normalized.

Examples:

Equivalent:

```
arroz
Arroz
ARROZ
```

Possible equivalent:

```
Melón
Melon
```

Require review:

```
Lenteja
Lentejas
```

Different:

```
Cava
Caña
```

---

# Product Identity

The product identifier must not depend on the name.

Example:

```
Product ID:

8f72a9d3
```

Name:

```
Arroz
```

If renamed:

```
Rice
```

The identity remains the same.

---

# Category

## Purpose

Groups products to help discovery.

Examples:

```
Vegetables

Dairy

Cleaning

Drinks
```

---

## Responsibilities

Categories provide:

- Organization.
- Product discovery.
- Filtering.

---

## Conceptual Model

```
Category

id

name

icon

createdAt
```

---

## Rules

Categories should be customizable.

Users may:

- Create.
- Rename.
- Delete.

---

# Shopping List

## Purpose

Represents a collection of products for a specific purchase intention.

Examples:

```
Weekly Shopping

Christmas Dinner

Beach Trip
```

---

## Characteristics

A shopping list is independent.

Duplicating a list creates a new entity.

---

## Conceptual Model

```
ShoppingList

id

name

type

createdAt

updatedAt
```

---

## List Types

Initial:

```
NORMAL
```

Future:

```
HOUSEHOLD

TEMPLATE

TEMPORARY
```

---

# Shopping Item

## Purpose

Represents a product inside a shopping list.

A ShoppingItem is not a Product.

Important distinction:

Product:

```
Milk
```

Shopping Item:

```
Buy Milk this week
```

---

## Conceptual Model

```
ShoppingItem

id

shoppingListId

productId

quantity

completed

createdAt

completedAt
```

---

## States

A shopping item has two main states:

Pending:

```
Not purchased
```

Completed:

```
Purchased
```

---

## Shopping Mode Behavior

When completed:

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

---

# Purchase

## Purpose

Represents a completed shopping session.

A purchase is historical information.

---

## Example

```
Supermarket:

Mercadona

Date:

2026-01-15

Total:

82.50€
```

---

## Conceptual Model

```
Purchase

id

date

totalAmount

supermarket

createdAt
```

---

## Rules

A purchase may exist without:

- Price.
- Supermarket.

Because users may choose not to provide this information.

---

# Purchase Item

## Purpose

Represents a product purchased during a purchase.

---

## Conceptual Model

```
PurchaseItem

id

purchaseId

productId

quantity

price
```

---

## Future Usage

Allows:

- Product history.
- Price evolution.
- Spending analysis.

---

# Note

## Purpose

Stores contextual information.

Examples:

```
Tomatoes are cheaper on Fridays.

Remember buying batteries.
```

---

## Conceptual Model

```
Note

id

title

content

createdAt

updatedAt
```

---

## Future Extensions

Notes may belong to:

- User.
- Household.
- Shopping list.
- Product.

---

# Household (Future)

## Purpose

Represents a group of people sharing shopping information.

Example:

```
Family Home
```

---

## Conceptual Model

```
Household

id

name

createdAt
```

---

## Contains

Future relationships:

```
Household

|

├── Users

├── Products

├── Lists

├── Notes

└── Purchases
```

---

# User (Future)

## Purpose

Represents a person using PantryHub.

---

## Conceptual Model

```
User

id

name

email

createdAt
```

---

# Invitation (Future)

## Purpose

Allows users to join households.

Possible methods:

- QR code.
- Link.
- Token.

---

## Conceptual Model

```
Invitation

id

householdId

token

expiresAt
```

---

# Synchronization Record (Future)

## Purpose

Tracks changes between devices.

---

## Conceptual Model

```
SyncRecord

id

entityId

entityType

operation

timestamp

status
```

---

# Entity Relationships

Initial version:

```
Category

   |

   |

Product

   |

   |

ShoppingItem

   |

   |

ShoppingList
```

---

Purchase history:

```
Product

   |

PurchaseItem

   |

Purchase
```

---

# Domain Rules

## Product Creation

Before creating a product:

Check:

- Exact duplicates.
- Normalized duplicates.
- Similar names.

---

## List Duplication

When duplicating:

Create:

```
New ShoppingList

New ShoppingItems
```

Reuse:

```
Existing Products
```

---

## Product Deletion

Deleting a product should consider:

Existing references:

- Shopping history.
- Previous purchases.
- Lists.

Possible strategies:

- Soft delete.
- Archive.

---

# Soft Delete Strategy

Recommended.

Instead of removing:

```
Product
```

store:

```
deleted = true
```

---

## Reason

Historical purchases should remain valid.

---

# Domain Events (Future)

Possible events:

```
ProductCreated

ProductAddedToList

ShoppingCompleted

PurchaseRecorded

HouseholdJoined
```

---

# Future Scalability Considerations

Entities should support:

- UUID identifiers.
- Creation timestamps.
- Modification timestamps.
- Synchronization metadata.

---

# Final Domain Vision

PantryHub is not based on lists.

Lists are only one way to interact with the real domain.

The true domain is:

```
Products

+

Shopping Intentions

+

Purchasing History

+

Household Knowledge
```

The application should preserve and grow this knowledge over time.

---
Last updated: July 26, 2026
