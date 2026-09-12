# PantryHub — Rework Action Plan

> Status: **APPROVED for phases R1 → R3 (ship 1.0 offline first).** Phases R4/R5
> (server + connected app) are deferred until the owner builds the shared
> infrastructure for all their projects; the backend stack decision is postponed.
> Companion to `STATUS.md` (current state) and `19_Execution_Plan.md` (sprint history).
>
> Date: 2026-09-11 · Approved: 2026-09-11

---

## 1. Why a rework

The app works end to end (lists, products, categories, aliases, notes, shopping mode,
import/export, settings, i18n, theming) and the shopping section already matches the
mockup. Three gaps remain:

1. **Visual & organizational.** Products, Notes and Settings still lag the mockup; the
   repository's documentation is internally inconsistent; several conventions (soft
   delete, migration policy, module naming) are documented but not real.
2. **Multi-device connection.** There is no network layer, no sync manager, no change
   tracking. The data model is **not** sync-ready even though `12_Synchronization.md`
   says 1.0 must prepare it.
3. **Data-safety.** `fallbackToDestructiveMigration()` is still enabled — a release with
   it can silently wipe a user's data on a schema change.

The owner's direction: a self-hosted (or interim cloud) server hosting **several
databases for several projects**; until it exists, do everything possible with the
current app.

---

## 2. Diagnosis (as of this analysis)

### 2.1 What is solid
- Multi-module Clean Architecture: 7 `core-*` + 5 `feature-*`, features never import
  each other, Hilt wiring across modules, single `UiState` per screen, sealed intents.
- Room v4 with explicit `MIGRATION_2_3` (aliases) and `MIGRATION_3_4` (notes).
- Repositories are interfaces bound to `Offline*` implementations via `DataModule`
  — a clean seam for swapping in synced implementations.
- UUID string ids on **every** entity (sync-friendly).
- Full JSON import/export round-trip with id remapping and duplicate detection
  (works today as the manual device-transfer mechanism).
- Design system with real identity (Nocturne / Warm Pantry, Space Grotesk + Inter) and
  shared components (`PantryItemCard`, `PantryBadge`, `PantryCheckbox`, `PantryTopBar`, …).

### 2.2 What blocks sync (code reality)

| Entity | `created_at` | `updated_at` | `deleted_at` (soft delete) | owner / household / version |
|---|---|---|---|---|
| products | ✅ | ❌ | ❌ | ❌ |
| categories | ❌ | ❌ | ❌ | ❌ |
| shopping_lists | ✅ | ❌ | ❌ | ❌ |
| shopping_items | `added_at` | ❌ | ❌ | ❌ |
| purchases / purchase_items | ✅ / ❌ | ❌ | ❌ | ❌ |
| notes | ✅ | ✅ | ❌ | ❌ |

- All deletes are physical (`@Delete` / `DELETE FROM`) with `ON DELETE CASCADE`.
- No `sync_queue` / outbox, no `SyncManager`, no `RemoteDataSource`, no Retrofit/Ktor,
  no WorkManager.
- No stable device identity.

### 2.3 Documentation drift (organizational debt)
- Root `README.md` claims "no production features implemented yet"; `docs/README.md`
  lists documents as pending that exist.
- `01_Roadmap.md` defines **v1.0 = households + sync**; `17`, `19` and `STATUS.md`
  define **1.0 = offline personal app**. `STATUS.md` is authoritative but `01` was never
  reconciled.
- `19_Execution_Plan.md` shows Sprints 1–6 unchecked and "~65–70%", while `STATUS.md`
  says ~96% done.
- `06_Technology.md` / `07_Architecture.md` describe modules `core`, `data`, `domain`
  — the real layout is `core-*` / `feature-*`.
- `08` vs `09`: soft delete described as `deleted = true` vs `is_deleted` vs `deleted_at`;
  `19 §9` claims "soft-delete already prepared" — false in code.
- `16_Release_Process.md` references `17_Changelog.md`; the file is `18_Changelog.md`.
- `09`/`16`/`19` forbid `fallbackToDestructiveMigration` in release; the code has it on.

---

## 3. Target architecture (where we are going)

```
Compose UI → ViewModel → Use cases → Repository
                                        ├── LocalDataSource   (Room, source of truth)
                                        └── RemoteDataSource  (Ktor client → REST /api/v1)
                                    SyncManager (WorkManager): outbox push · cursor pull · merge
```

- **Offline-first, local source of truth** (unchanged principle from `10`/`12`).
- **Change tracking** on every synced entity: `created_at`, `updated_at`, `deleted_at`
  (soft delete), later `owner_id` / `household_id`.
- **Outbox** (`sync_queue`): every local write enqueues an op; the manager pushes in
  batches and pulls a server change log by cursor.
- **Conflicts**: last-write-wins by `updated_at` (device id as tie-break) for lists,
  items and categories; products and notes flagged for user resolution in a later
  iteration (as `12` recommends).
- **Server**: one VPS, Docker, **one PostgreSQL server with one database (or schema)
  per project** — PantryHub gets its own DB; the API is its own container behind an
  auto-TLS reverse proxy.

---

## 4. Tracks

### Track A — Organizational rework (docs + repo hygiene) · *no server needed*
- A1. Reconcile `01_Roadmap.md` with `STATUS.md`/`19`: **1.0 = offline**, **1.1 =
  connected (accounts + multi-device)**, **1.2 = households/sharing/QR**.
- A2. Rewrite root `README.md` and `docs/README.md` to reflect reality (modules, features,
  how to build, where STATUS lives).
- A3. Fix `06`/`07` module layout; fix `16` changelog reference; fix `19` checkbox state
  (mark Sprints 1–6 + VP-0..2 done).
- A4. Decide and document **one** soft-delete convention: `deleted_at: Instant?` (nullable
  timestamp). Update `08`, `09`, `12`.
- A5. Document the migration policy as **enforced**: no destructive fallback in release
  (see C7).
- A6. Add `20_Rework_Plan.md` (this file) and, once approved, fold tracks into `19`.

### Track B — Visual rework · *no server needed*

> **Superseded on 2026-09-11 by a full redesign.** Incremental polish of the old
> composition did not reach a professional feel, so the whole app was redesigned on a design
> canvas ("PantryHub Redesign", 8 screens) and approved by the owner. The approved system is
> specified in `05_Design_System.md` §6 (composition, cards, rows, inputs, sheets,
> navigation, grouping/filtering) and `04_UX_Guidelines.md` (navigation rules, category
> browsing). Track B is now: implement that system screen by screen — composition
> components first, then Lists → List detail → Shopping mode → Products → Notes →
> Settings/Help, with bottom sheets replacing dialogs — followed by the cross-cutting items
> below (B4, B5). The sub-items B1–B3 remain as the per-screen checklist.
- B0. ✅ **Shopping flow** (Lists, List detail, Shopping mode) in the new design, with the
  "shopping in progress" rule (`05` §6.2) and the finish sheet.
- B1. ✅ **Products + Categories**: header with live counts, Categories button + filter
  button, permanent search pill, grouping (Favorites → categories → No category), filter
  sheet (category · sort · "Show N products" · Reset · Manage categories), New product /
  Edit product sheets, long-press actions sheet, multi-select with "Assign category" and
  bulk delete, dedicated **Categories screen** (`Destination.Categories`), swipe actions via
  `PantrySwipeRow`.
- B2. ✅ **Notes**: 2-column grid of cards (title, excerpt, relative date), header search
  toggle, editor sheet with delete-with-confirmation.
- B3. ✅ **Settings + Help + Backup**: grouped cards with icon tiles, theme/language sheets,
  **Manage data** sheet (multi-select wipe: lists, products, categories, notes, history,
  favorites, everything — `ClearDataUseCase`), about row with version; Help and Backup
  screens use the compact top bar + header.
- B4. **Cross-cutting** (next): remove `PantryTopBar`, `PantrySearchBar`, `PantryCard`
  where unused and the leftover strings (`pending_*`, `*_coming_soon_*`, `shopping_mode_title`…);
  list item enter/exit animations everywhere (`animateItem` is in Products, Notes and
  Shopping mode); undo snackbar for deletions; consistent empty/error states; light-theme
  (Warm Pantry) pass on every screen.
- B5. **Accessibility**: `contentDescription` audit, 48dp targets, contrast check on
  badges/dots, dynamic text.

### Track C — Sync-ready data foundation · **DEFERRED to R4** (owner decision 2026-09-11)

> The owner asked to focus on the app itself, not on server/sync preparation. Everything
> in this track except **C7** (data safety, part of the 1.0 release checklist) waits
> until the shared infrastructure exists. Kept here as the agreed design.

- C1. **Room v4 → v5 migration**: add `updated_at` (backfilled from `created_at` /
  `added_at`) and `deleted_at` (nullable) to products, categories, shopping_lists,
  shopping_items, purchases, purchase_items, notes; add `created_at` to categories and
  purchase_items.
- C2. **Soft delete**: repositories set `deleted_at` instead of deleting; DAO reads filter
  `deleted_at IS NULL`; deleting a list soft-deletes its items (replaces CASCADE
  semantics for synced entities). A periodic purge of old tombstones can come later.
- C3. **Touch `updated_at` on every write** in the `Offline*` repositories (single helper).
- C4. **Outbox**: `sync_queue` table (`id`, `entity_type`, `entity_id`, `operation`,
  `created_at`, `status`, `attempts`) + DAO; repositories enqueue on write. Inert until
  sync is enabled (no behavior change for users).
- C5. **Seams**: split each `Offline*Repository` into `LocalDataSource` + repository;
  add `RemoteDataSource` interfaces with a `NoOpRemoteDataSource`; add a `SyncManager`
  interface with a `NoOpSyncManager`; bind via Hilt. Zero runtime change today.
- C6. **Device identity**: stable `deviceId` (UUID in DataStore) for LWW tie-breaks and
  future pairing.
- C7. **Remove `fallbackToDestructiveMigration()` from release** (keep behind
  `BuildConfig.DEBUG` only) and add a Room migration test for 2→3→4→5.
- C8. **Backup schema**: bump `BackupData.version` to 2, include the new fields; importer
  accepts v1 and v2.

### Track D — Server + connected app · *starts when the server exists*
- D1. **Infrastructure**: VPS (Hetzner / OVH / DigitalOcean class), Docker Compose:
  `postgres` (shared across projects, one DB per project) · `pantryhub-api` · reverse
  proxy with automatic TLS (Caddy or Traefik) · nightly `pg_dump` backups.
- D2. **Backend** — recommended **Kotlin + Ktor + PostgreSQL** (Exposed or jOOQ):
  reuses the team's Kotlin, matches the docs' "Kotlin backend" intent, one Postgres for
  many projects. Alternatives: **PocketBase** (fastest, SQLite-per-instance, auth +
  REST built in — good interim if speed matters more than control) or **Supabase
  self-hosted** (heavier). Decision D-1 below.
- D3. **API v1**: JWT auth (email + password; device auth later), CRUD resources per
  `10_API.md`, and the sync pair `POST /api/v1/sync/push` (batch of outbox ops) +
  `GET /api/v1/sync/pull?since=<cursor>` (server change log, includes tombstones).
- D4. **Android**: `core-network` (Ktor client + kotlinx.serialization), `core-sync`
  (`SyncManager` impl, WorkManager: on app start, on connectivity, periodic),
  `feature-account` (sign in / register / device list), Settings → Account & Sync
  (status, last synced, sign out). Real `RemoteDataSource` implementations replace the
  no-ops via Hilt.
- D5. **Security**: HTTPS only, tokens in EncryptedDataStore/Keystore, refresh/revoke,
  per-user data isolation, rate limiting (per `14_Security.md`).
- D6. **Households / sharing / QR** (later release): `Household`, membership + roles,
  `owner_id` / `household_id` on entities, QR invitation per `13_QR.md`.

### Track E — 1.0 closing (quality + release) · *no server needed*
- E1. Unit tests: use cases (clone, finish shopping incl. one-off deletion, import
  remap), Jaro-Winkler similarity, mappers, ViewModels (Turbine).
- E2. Room migration tests (C7) and import/export round-trip test.
- E3. Compose UI smoke tests: lists, detail, shopping mode, import.
- E4. Release prep: app icon + splash, `versionName`/`versionCode`, signing (key outside
  the repo), Play listing (screenshots light/dark, privacy policy), Internal → Beta.

---

## 5. Phasing (recommended order)

| Phase | Tracks | Outcome | Server? |
|---|---|---|---|
| **R1 · Organize + secure** | A + C7 | Consistent docs and conventions; no destructive migration fallback in release | No |
| **R2 · Visual complete** | B | Whole app at mockup level, accessible | No |
| **R3 · Ship 1.0 (offline)** | E | Tests + release assets → Play Internal/Beta | No |
| **R4 · Sync foundation + Connected 1.1** | C (rest) + D1–D5 | Sync-ready schema, accounts, personal multi-device sync | **Yes** (deferred) |
| **R5 · Household 1.2** | D6 | Sharing, roles, QR invitations | Yes (deferred) |

R1–R3 are app-only and are what we execute now. R4/R5 wait for the owner's shared
infrastructure decision. When R4 starts, the Track C schema work (Room v5) ships as a
normal tested migration.

Rough effort (working sessions): R1 ≈ 2–3 · R2 ≈ 5–7 · R3 ≈ 4–5 · R4 ≈ 12–18 (schema +
API + Android) · R5 ≈ 6–8.

---

## 6. Decisions

| # | Decision | Outcome (2026-09-11) |
|---|---|---|
| D-1 | Backend stack | **Deferred.** The owner will host several Android apps, web projects, APIs and databases on shared infrastructure and will choose the stack when building it. Track D stays as documented guidance only. |
| D-2 | Order | **Ship 1.0 offline first** (R1 → R2 → R3), then connect. |
| D-3 | First connected scope | **Deferred** (no preference yet). Track C prepares the schema so either scope is possible. |
| D-4 | Soft-delete convention | **`deleted_at: Instant?`** (nullable timestamp), as `12_Synchronization.md` recommends. |
| D-5 | Destructive fallback | **Removed from release builds in R1**; kept only behind `BuildConfig.DEBUG`. Every future schema change ships with a tested migration. |

---

## 7. Out of scope for this plan
- Price tracking, analytics/reports, recipes, widgets, tablet layouts (roadmap 1.3+).
- Real-time push (WebSockets/FCM) — pull-based sync first; real-time later.
