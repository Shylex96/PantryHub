# PantryHub Import & Export

## Overview

This document defines the import and export system of PantryHub.

The purpose of this feature is to allow users to:

- Backup their information.
- Move data between devices.
- Share shopping knowledge with other people.
- Restore previous states.
- Prepare the foundation for future synchronization.

---

# Import / Export Philosophy

## User Owns The Data

Users should always be able to:

- Export their information.
- Keep a personal backup.
- Import data whenever needed.

The application should not lock user information.

---

## Human Readable Format

The export format should be:

```
JSON
```

Reasons:

- Easy to understand.
- Portable.
- Future compatible.
- Easy to validate.

---

# Export Scope

The export should contain all relevant user data.

Version 1:

```
Products

Categories

Shopping Lists

Shopping Items

Notes

Purchase History
```

Future:

```
Households

Users

Synchronization Data
```

---

# Export File

Recommended filename:

Example:

```
pantryhub_backup_2026-01-01.json
```

---

# JSON Structure

Initial structure:

```json
{
  "schemaVersion": 1,
  "exportDate": "2026-01-01T10:00:00",
  "applicationVersion": "1.0.0",
  "data": {
    "products": [],
    "categories": [],
    "shoppingLists": [],
    "shoppingItems": [],
    "notes": [],
    "purchases": []
  }
}
```

---

# Schema Versioning

Every export must include:

```
schemaVersion
```

Example:

```
1
```

---

## Purpose

Allows future migrations.

Example:

Version 1:

```
product.name
```

Version 2:

```
product.displayName
```

The importer can adapt old files.

---

# Export Validation

Before generating the file:

Validate:

- Database consistency.
- Required fields.
- Relationships.

---

Example:

A shopping item without product reference:

```
Invalid export
```

---

# Import Process

The import process should not directly overwrite data.

Flow:

```
Select File

↓

Validate

↓

Analyze

↓

Show Conflicts

↓

User Confirmation

↓

Import
```

---

# Import Modes

The application should support different strategies.

---

# Replace Mode

Purpose:

Restore a complete backup.

Example:

New phone.

Behaviour:

```
Delete local data

Import backup
```

---

## Warning

This is destructive.

Requires confirmation.

---

# Merge Mode

Purpose:

Combine existing data with imported information.

Example:

Family member sharing products.

Behaviour:

```
Existing data

+

Imported data

=

Combined data
```

---

# Duplicate Detection

During import, PantryHub must detect possible duplicates.

Examples:

Exact duplicate:

```
Arroz

Arroz
```

Automatic merge.

---

Accent duplicate:

```
Melón

Melon
```

Possible merge.

---

Similar:

```
Lenteja

Lentejas
```

Requires user decision.

---

Different:

```
Cava

Caña
```

Keep separate.

---

# Product Matching Algorithm

The matching process should consider:

## Step 1

Normalize:

```
lowercase

remove accents

trim spaces

remove repeated spaces
```

Example:

```
  MELÓN
```

becomes:

```
melon
```

---

## Step 2

Exact normalized match.

Example:

```
melon

melon
```

Automatic merge.

---

## Step 3

Similarity detection.

Possible algorithms:

- Levenshtein distance.
- Jaro-Winkler.
- Token similarity.

---

# Similarity Thresholds

Initial proposal:

```
0-70%

Different products

70-90%

Review required

90-100%

Possible duplicate
```

---

These thresholds should be configurable after testing.

---

# Import Conflict Screen

When conflicts exist:

Show:

```
Possible duplicate found

Existing:

Lenteja


Imported:

Lentejas


Actions:

Keep existing

Import new

Merge manually
```

---

# Merge Rules

When merging products:

Keep:

```
Existing ID
```

Update:

```
Missing information
```

Combine:

```
Categories

Usage count

Favorites
```

---

# List Import Behavior

When importing lists:

Example:

Existing:

```
Weekly Shopping
```

Imported:

```
Weekly Shopping
```

The user decides:

Options:

```
Create copy

Merge items

Replace existing
```

---

# Shopping Item Import

Shopping items depend on:

```
Shopping List

Product
```

Order:

1. Import products.
2. Resolve categories.
3. Import lists.
4. Import items.
5. Import history.

---

# Import Errors

Errors should not abort the complete process.

Example:

```
100 products processed

98 imported

2 require attention
```

---

# Import Report

After completion show:

Example:

```
Import completed

Products added: 45

Products merged: 12

Lists created: 3

Conflicts: 2
```

---

# Security

Imported files should be treated as external data.

Validate:

- JSON structure.
- Data size.
- Invalid references.

---

# Future Household Import

When household synchronization exists:

Import should support:

```
Personal data

+

Household data
```

with clear separation.

---

# Backup Strategy

Future possibilities:

- Automatic backups.
- Scheduled exports.
- Cloud backup.

---

# Compatibility Rules

The importer should always try to support:

```
Current version

+

Previous versions
```

---

# Data Ownership

Imported data should maintain:

```
createdAt

updatedAt

original source
```

Future field:

```
originDevice
```

---

# Import / Export Testing

Must test:

## Complete Export

Export all data.

Import into empty database.

Result:

```
Equivalent database
```

---

## Partial Export

Only some entities.

Result:

```
Valid import
```

---

## Old Version Import

Older schema.

Result:

```
Migrated successfully
```

---

# Version 1.0 Requirements

The first implementation must support:

- Export complete JSON.
- Import JSON.
- Schema versioning.
- Duplicate detection.
- Basic merge.
- Conflict reporting.

---

# Final Goal

Import and export should make PantryHub feel like:

"Your shopping memory belongs to you."

---
Last updated: July 26, 2026
