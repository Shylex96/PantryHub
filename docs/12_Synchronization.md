# PantryHub Synchronization

> **Status (2026-09-11): design only — nothing here is implemented.** There is no network
> layer, no sync queue / outbox, no change tracking, no `deleted_at` and no `updated_at`
> on entities other than `notes`. The sync-ready data model (Room v5) and personal
> multi-device sync are deferred to rework phase **R4 / version 1.1**; household sharing to
> **R5 / version 1.2**. See `20_Rework_Plan.md` and `STATUS.md`. Version 1.0 ships offline only.

## Overview

This document defines the future synchronization architecture of PantryHub.

Synchronization allows PantryHub to evolve from:

```
Single-device shopping application
```

into:

```
Shared household shopping platform
```

The synchronization system must support:

- Multiple devices.
- Multiple users.
- Shared shopping lists.
- Shared products.
- Offline usage.
- Conflict resolution.

---

# Synchronization Philosophy

## Offline First

PantryHub must work completely without an internet connection.

The application must never block the user because synchronization is unavailable.

---

## Local First Architecture

The local database remains the main source of truth.

Flow:

```
User Action

↓

Local Room Database

↓

UI Update

↓

Synchronization Queue

↓

Remote Server

↓

Other Devices
```

---

# Import / Export vs Synchronization

These are different systems.

---

# Import / Export

Purpose:

```
Move or backup data manually
```

Characteristics:

- User initiated.
- File based.
- Occasional.
- No permanent connection.

Example:

```
Export JSON

Send to family member

Import JSON
```

---

# Synchronization

Purpose:

```
Keep multiple devices updated automatically
```

Characteristics:

- Continuous.
- Background process.
- Requires identity.
- Requires conflict handling.

Example:

```
User A adds milk

↓

User B sees milk automatically
```

---

# Synchronization Requirements

The system must support:

## Personal Data

Example:

```
My private shopping lists
```

---

## Shared Household Data

Example:

```
Family shopping list
```

---

## Multiple Devices

Example:

```
Phone

Tablet

Second phone
```

---

# Household Concept

Synchronization introduces the concept of:

```
Household
```

A household represents a group of users sharing information.

Examples:

```
Family Home

Roommates

Couple
```

---

# Household Ownership

A household contains:

```
Owner

Members

Shared Data
```

---

## Shared Data

Possible shared entities:

```
Products

Categories

Shopping Lists

Shopping Items

Notes

Purchases
```

---

# Personal vs Shared Data

Entities should define ownership.

Example:

Product:

```
Personal Product
```

or:

```
Household Product
```

---

Shopping list:

```
Private List
```

or:

```
Shared Household List
```

---

# Future Entity Model

```
User

 |

 |

Household

 |

 |

-------------------

Products

Lists

Notes

Purchases

-------------------
```

---

# Synchronization Architecture

Future architecture:

```
                 Remote API

                     |

             Sync Manager

                     |

              Repository

                     |

             Local Database
```

---

# Sync Manager

## Purpose

Coordinates synchronization.

Responsibilities:

- Detect local changes.
- Upload changes.
- Download remote changes.
- Resolve conflicts.
- Update local database.

---

# Synchronization Queue (not implemented)

Local database should maintain pending operations. This table does not exist yet; it is added in R4.

Example:

Table:

```
sync_queue
```

---

## Fields

Conceptually:

```
id

entity_id

entity_type

operation

created_at

status
```

---

# Operations

Possible operations:

```
CREATE

UPDATE

DELETE
```

---

Example:

User creates product:

```
CREATE Product

↓

Queue

↓

Upload later
```

---

# Synchronization Flow

## Local Creation

Example:

User creates:

```
Rice
```

Process:

```
Create product locally

↓

Mark pending sync

↓

Upload when possible

↓

Server confirms

↓

Remove from queue
```

---

# Remote Changes

Example:

Another family member creates:

```
Milk
```

Process:

```
Receive remote change

↓

Validate

↓

Update Room

↓

UI automatically refreshes
```

---

# Conflict Resolution

Conflicts happen when:

Two devices modify the same entity.

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

# Conflict Strategies

## Strategy 1

## Last Write Wins

The newest modification wins.

Advantages:

- Simple.
- Fast.

Disadvantages:

- Possible data loss.

---

## Strategy 2

## User Resolution

Recommended for important entities.

Example:

```
Conflict detected

Version A:

Milk

Version B:

Whole Milk


Choose one
```

---

# Entity Conflict Rules

Not every entity needs the same strategy.

---

## Products

Prefer:

```
Manual resolution
```

because products represent knowledge.

---

## Shopping Items

Prefer:

```
Automatic merge
```

because completion state is simple.

Example:

Device A:

```
Completed
```

Device B:

```
Pending
```

Result:

```
Completed
```

---

## Notes

Prefer:

```
Version history
```

to avoid losing text.

---

# Synchronization Metadata

Sync-ready entities will support:

```
created_at

updated_at

deleted_at   (nullable timestamp)

owner_id

version
```

Current schema (Room v4): every entity has `created_at`; only `notes` has `updated_at`; no entity has `deleted_at`, `owner_id` or `version`. These are added together in R4 (Room v5).

---

# Soft Delete (decided convention, not yet implemented)

Deletion should be synchronized.

Never immediately remove shared entities.

Convention (consistent with `08_Domain_Model.md` and `09_Database.md`):

```
deleted_at: nullable timestamp
```

Instead of:

```
DELETE Product
```

use:

```
deleted_at = timestamp   (null = live row)
```

Default queries filter `deleted_at IS NULL`. Today deletes are physical; soft delete arrives with Room v5 in R4.

---

# Why Soft Delete?

Required for:

- Synchronization.
- Conflict resolution.
- Recovery.
- History.

---

# Real Time Updates

Future implementations may use:

- WebSockets.
- Firebase listeners.
- Push notifications.

---

Possible flow:

```
Server Event

↓

Push Notification

↓

Sync Manager

↓

Room Update

↓

UI Update
```

---

# QR Code Usage

QR codes may be used as a synchronization entry point.

Possible purposes:

## Household Invitation

Example:

```
Scan QR

↓

Join Household
```

---

## Temporary Sharing

Example:

```
Share shopping list
```

---

## Device Pairing

Example:

```
Connect another device
```

---

# Synchronization Security

Requirements:

- Authentication required.
- Household authorization.
- Encrypted communication.
- Permission checks.

---

# Permissions Model

Future household roles:

```
Owner

Administrator

Member

Viewer
```

---

# Example Permissions

Owner:

```
Manage members

Delete household

Change settings
```

---

Member:

```
Add products

Modify lists

Complete purchases
```

---

Viewer:

```
Read only
```

---

# Synchronization Errors

Errors should not break local usage.

Example:

Network unavailable:

```
Shopping continues normally.

Changes will synchronize later.
```

---

# Background Synchronization

Possible triggers:

- Application startup.
- Network available.
- Periodic background task.

Technology:

```
WorkManager
```

---

# Synchronization Testing

Must test:

## Offline Creation

Create item offline.

Expected:

```
Synchronizes later
```

---

## Concurrent Modification

Two devices modify same item.

Expected:

```
Conflict handled correctly
```

---

## Data Recovery

Delete and restore.

Expected:

```
No broken references
```

---

# Version 1.0 Scope

Synchronization is NOT part of 1.0 (decision 2026-09-11: 1.0 is the offline personal app).

What 1.0 already provides that sync will build on:

- UUID identifiers (string ids on all entities).
- `created_at` timestamps.
- Repository abstraction (interfaces in `core-domain`, `Offline*` implementations bound in `core-data`) — the seam for synced implementations.
- Import/export compatibility.

What 1.0 does **not** include (deferred to R4 / 1.1, see `20_Rework_Plan.md`):

- `updated_at` on all entities (only `notes` has it).
- Soft delete (`deleted_at`).
- Sync queue / outbox and change tracking.
- Network layer, accounts and sync manager.

Version 1.1 = accounts + personal multi-device sync. Version 1.2 = households / sharing / QR.

---

# Future Synchronization Goal

PantryHub synchronization should allow:

```
Everyone in the household

↓

Sharing the same shopping memory

↓

Without manual exports
```

while preserving:

- User control.
- Offline functionality.
- Data ownership.

---
Last updated: September 11, 2026
