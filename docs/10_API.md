# PantryHub API Design

## Overview

This document defines the future API architecture for PantryHub.

The first versions of the application are designed as an offline-first Android application.

No backend dependency is required for the initial release.

However, the architecture must allow future implementation of:

- Cloud synchronization.
- Household collaboration.
- Multi-device usage.
- User accounts.
- Shared shopping spaces.

---

# API Philosophy

The API should follow these principles:

## Offline First

The application should never depend completely on network availability.

The expected flow:

```
User Action

↓

Local Database Update

↓

Background Synchronization

↓

Remote API
```

---

## Local Source of Truth

The Android application should always work from local data.

Example:

```
Room Database

↓

Repository

↓

UI
```

The API should synchronize information, not replace local storage.

---

## User Ownership

Users own their data.

The API must support:

- Export.
- Migration.
- Deletion.
- Privacy controls.

---

# Future Backend Responsibilities

The backend should handle:

- Authentication.
- User accounts.
- Household groups.
- Synchronization.
- Conflict resolution.
- Cloud backups.

The backend should not replace local application logic.

---

# API Style

Recommended architecture:

```
REST API
```

using:

```
JSON
```

communication.

---

## Future Technology

Possible backend implementation:

```
Kotlin + Spring Boot

or

Node.js / TypeScript

or

Cloud Functions
```

The mobile application should remain independent of backend technology.

---

# Communication Layer

Android side:

```
Retrofit

+

OkHttp

+

Kotlin Serialization
```

---

## API Module

Future structure:

```
data

└── remote

    ├── api

    ├── dto

    └── mapper
```

---

# Data Flow

Complete future architecture:

```
Compose UI

↓

ViewModel

↓

Use Case

↓

Repository

↓

---------------------

Local Data Source

(Room)

        +

Remote Data Source

(Retrofit)

---------------------

↓

Repository

↓

Domain
```

---

# API Versioning

The API should use versioning.

Example:

```
/api/v1/
```

Future breaking changes:

```
/api/v2/
```

---

# Authentication

Future authentication system.

Possible methods:

- Email/password.
- OAuth.
- Device-based authentication.

---

## Authentication Token

Recommended:

```
JWT
```

or equivalent secure token system.

---

## Token Rules

The application should:

- Store tokens securely.
- Refresh expired tokens.
- Logout safely.

---

# Main Resources

Future API resources:

```
Users

Households

Products

Categories

Shopping Lists

Shopping Items

Purchases

Notes

Synchronization
```

---

# User Resource

## Purpose

Represents an application user.

---

## Example

```
User

id

name

email

createdAt
```

---

# Household Resource

## Purpose

Represents a shared environment.

Example:

```
Family Home
```

---

## Example

```
Household

id

name

createdAt

ownerId
```

---

# Product Resource

## Purpose

Synchronizable product information.

---

## Example

```json
{
  "id": "uuid",
  "name": "Milk",
  "normalizedName": "milk",
  "categoryId": "uuid",
  "updatedAt": "2026-01-01T10:00:00"
}
```

---

# Shopping List Resource

## Purpose

Represents a shared or personal list.

---

## Example

```json
{
  "id": "uuid",
  "name": "Weekly Shopping",
  "type": "NORMAL",
  "updatedAt": "2026-01-01T10:00:00"
}
```

---

# Shopping Item Resource

## Purpose

Represents a product inside a list.

---

## Example

```json
{
  "id": "uuid",
  "listId": "uuid",
  "productId": "uuid",
  "completed": false
}
```

---

# Purchase Resource

## Purpose

Stores historical purchases.

---

## Example

```json
{
  "id": "uuid",
  "date": "2026-01-01",
  "amount": 75.50,
  "supermarket": "Example"
}
```

---

# Synchronization API

Synchronization is the most complex future feature.

The API must support:

- New data.
- Modified data.
- Deleted data.
- Conflicts.

---

# Synchronization Strategy

Recommended model:

```
Change Tracking
```

Each entity contains:

```
createdAt

updatedAt

deletedAt
```

---

## Synchronization Flow

Example:

User edits product:

```
Android

↓

Local Room update

↓

Sync queue

↓

API upload

↓

Server validation

↓

Other devices updated
```

---

# Conflict Resolution

Conflicts may happen when:

Two devices modify the same object.

Example:

Device A:

```
Milk
```

Device B:

```
Whole Milk
```

---

Possible strategies:

## Last Write Wins

Simpler.

Most recent update wins.

---

## User Resolution

Recommended for important data.

Example:

```
Two versions found

Keep:

Option A

Option B
```

---

# Import / Export Compatibility

The API should share compatibility rules with JSON export.

Both should support:

```
schemaVersion
```

Example:

```json
{
  "schemaVersion": 1
}
```

---

# Security Requirements

Future API must implement:

- HTTPS only.
- Secure authentication.
- Authorization checks.
- Data isolation between households.
- Input validation.

---

# Rate Limiting

The API should protect resources.

Examples:

- Prevent abusive synchronization.
- Limit repeated requests.

---

# Offline Queue

The application should maintain pending operations.

Example:

```
sync_queue
```

Stores:

```
entity

operation

timestamp

status
```

---

# API Error Handling

Errors should be predictable.

Example:

```json
{
  "code": "PRODUCT_ALREADY_EXISTS",
  "message": "Product already exists"
}
```

---

# Analytics API

Future possibility.

Used for:

- Anonymous statistics.
- Product trends.
- Usage insights.

Must respect:

- User privacy.
- Explicit permissions.

---

# API Not Required For Version 1.0

The following features do not require API:

- Products.
- Lists.
- Categories.
- Shopping mode.
- Notes.
- Import/export.

They remain local.

---

# Future API Goal

The API should transform PantryHub from:

```
Single-device shopping assistant
```

into:

```
Connected household purchasing platform
```

while preserving:

- Offline functionality.
- User ownership.
- Data reliability.

---
Last updated: July 26, 2026
