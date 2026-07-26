# PantryHub Database Design

## Overview

This document defines the local database architecture of PantryHub.

The database is responsible for storing the user's shopping knowledge while maintaining:

- Data integrity.
- Offline availability.
- Fast searching.
- Future synchronization compatibility.
- Historical information preservation.

The initial persistence solution is:

```
Room Database
```

with:

```
SQLite
```

as the underlying database engine.

---

# Database Principles

## Local First

The local database is the source of truth.

Application flow:

```
Room Database

↓

Repository

↓

Use Case

↓

ViewModel

↓

UI
```

---

## Preserve History

Historical data should not be destroyed unnecessarily.

Example:

A product may be removed from active use but should remain valid inside previous purchases.

---

## Avoid Data Duplication

Products should exist once.

Lists and purchases reference products instead of copying product information.

---

# Database Structure

Initial entities:

```
ProductEntity

CategoryEntity

ShoppingListEntity

ShoppingItemEntity

PurchaseEntity

PurchaseItemEntity

NoteEntity
```

Future entities:

```
UserEntity

HouseholdEntity

SyncEntity
```

---

# Database Naming Convention

Tables:

```
snake_case
```

Examples:

```
products

shopping_lists

shopping_items
```

Columns:

```
snake_case
```

Examples:

```
created_at

updated_at
```

---

# Common Fields

All main entities should include:

```
id

created_at

updated_at
```

---

## Identifier Strategy

Recommended:

```
UUID
```

Example:

```
550e8400-e29b-41d4-a716-446655440000
```

---

## Reason

UUIDs allow future:

- Synchronization.
- Multiple devices.
- Conflict resolution.

---

# ProductEntity

## Purpose

Stores reusable products.

---

## Table

```
products
```

---

## Fields

Conceptual structure:

```
ProductEntity

id

name

normalized_name

category_id

is_favorite

usage_count

last_used_at

is_deleted

created_at

updated_at
```

---

# Product Rules

## Name

The displayed name:

Example:

```
Melón
```

---

## Normalized Name

Used internally for comparison.

Examples:

```
melón

melon

MELON
```

become:

```
melon
```

---

## Purpose

Allows duplicate detection.

---

# Product Search

Search priority:

1. Exact match.
2. Normalized match.
3. Partial match.
4. Frequency.
5. Alphabetical order.

---

# Product Indexes

Recommended indexes:

```
products.name

products.normalized_name

products.category_id

products.is_favorite
```

---

# CategoryEntity

## Purpose

Groups products.

---

## Table

```
categories
```

---

## Fields

```
id

name

normalized_name

icon

created_at

updated_at
```

---

# Category Rules

Categories are user editable.

Possible actions:

- Create.
- Rename.
- Archive.

---

# ShoppingListEntity

## Purpose

Represents a shopping intention.

---

## Table

```
shopping_lists
```

---

## Fields

```
id

name

type

is_archived

created_at

updated_at
```

---

# List Types

Initial:

```
NORMAL
```

Future:

```
TEMPLATE

TEMPORARY

HOUSEHOLD
```

---

# ShoppingItemEntity

## Purpose

Represents a product inside a list.

---

## Table

```
shopping_items
```

---

## Fields

```
id

shopping_list_id

product_id

quantity

is_completed

completed_at

created_at

updated_at
```

---

# Relationships

```
ShoppingList

1

|

N

ShoppingItem

N

|

1

Product
```

---

# Shopping Mode Queries

The database should support:

Pending items:

```
is_completed = false
```

Completed items:

```
is_completed = true
```

---

## Ordering

Default:

```
Pending first

Completed last
```

---

# PurchaseEntity

## Purpose

Stores completed shopping sessions.

---

## Table

```
purchases
```

---

## Fields

```
id

date

total_amount

supermarket

same_supermarket

created_at
```

---

# Purchase Rules

Price is optional.

Supermarket is optional.

A purchase can exist without financial information.

---

# PurchaseItemEntity

## Purpose

Stores products purchased during a purchase.

---

## Table

```
purchase_items
```

---

## Fields

```
id

purchase_id

product_id

quantity

price

created_at
```

---

# Historical Integrity

Purchase items should reference existing products.

Deleting a product should not invalidate previous purchases.

---

# NoteEntity

## Purpose

Stores user notes.

---

## Table

```
notes
```

---

## Fields

```
id

title

content

related_product_id

related_list_id

created_at

updated_at
```

---

# Relationships

Possible:

```
Product

1

|

N

Notes
```

and:

```
ShoppingList

1

|

N

Notes
```

---

# Database Relations Summary

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



Product

    |

    |

PurchaseItem

    |

    |

Purchase
```

---

# Room Architecture

Recommended structure:

```
data

├── local

│

├── database

│

├── dao

│

├── entity

│

└── mapper
```

---

# DAO Responsibilities

DAOs should only handle:

- Database operations.
- Queries.
- Persistence.

They should not contain:

- Business rules.
- UI logic.
---

# Example DAO Operations

Product DAO:

```
insertProduct()

updateProduct()

deleteProduct()

searchProducts()

observeProducts()
```

---

Shopping List DAO:

```
createList()

getLists()

deleteList()

duplicateList()
```

---

# Database Migrations

Room migrations must always be explicit.

Never use:

```
fallbackToDestructiveMigration
```

for production.

---

## Migration Strategy

Every schema change requires:

```
Migration_X_Y
```

Example:

```
Migration_1_2
```

---

# Import / Export Compatibility

The database should support JSON export.

Export should include:

- Schema version.
- Entities.
- Relationships.

Example:

```
{
 "version":1,
 "products":[],
 "lists":[]
}
```

---

# Future Synchronization Preparation

Entities should contain:

```
created_at

updated_at

deleted_at
```

---

## Soft Delete

Recommended instead of physical deletion.

Example:

```
is_deleted = true
```

---

## Reason

Required for:

- Sync.
- History.
- Conflict resolution.

---

# Duplicate Detection Support

Database should store:

```
normalized_name
```

to support:

- Accent removal.
- Case normalization.
- Similarity algorithms.

---

# Future Full Text Search

Possible improvement:

```
SQLite FTS5
```

for advanced search.

Benefits:

- Faster searches.
- Better matching.
- Large product databases.

---

# Performance Considerations

Important indexes:

Products:

```
normalized_name
category_id
usage_count
```

Shopping:

```
shopping_list_id
is_completed
```

History:

```
purchase.date
```

---

# Database Security

Local database should:

- Avoid unnecessary sensitive information.
- Be backed up only with user permission.
- Support encrypted storage in the future.

Possible future:

```
SQLCipher
```

---

# Database Version 1 Goal

The first database version should support:

- Products.
- Categories.
- Shopping lists.
- Shopping items.
- Shopping mode.
- Favorites.
- Notes.
- Purchase history foundation.

---

# Final Database Vision

The database is not only storage.

It is the memory of PantryHub.

It must preserve:

```
What users buy

+

How they organize purchases

+

How their habits evolve
```

while remaining ready for future collaboration and intelligence.

---
Last updated: July 26, 2026
