# ADR-016: Keep Room, migrate to Room KMP on 2.8

## Status
Proposed — 2026-09-13

## Context
`ADR-002` chose Room. The database is at version 4 with explicit `MIGRATION_2_3` and
`MIGRATION_3_4`, 8 entities, 5 DAOs and real user data behind them.

The audit found that 18 of the 19 files in `core-database` import nothing but
`androidx.room.*` — entities, relations, DAOs, converters and `PantryHubDatabase` all carry
over to Room KMP unchanged. The single Android-specific file is `di/DatabaseModule.kt`
(`Context`, `ApplicationInfo`, `SupportSQLiteDatabase` inside the migrations,
`fallbackToDestructiveMigration`, Hilt).

Room 2.8 is already KMP-capable and ships the driver APIs. Room **3.0** (March 2026) is the
modernised line: package `androidx.room3`, `@ColumnTypeConverter` in place of `@TypeConverter`,
`suspend` migrations, KSP-only. Google's own guidance is a two-phase move — modernise on 2.8
first, switch to 3.0 separately.

## Decision
**Keep Room. Migrate to Room KMP on the 2.8 line.** Do not replace it with SQLDelight and do
not jump to Room 3.0 as part of this migration.

Concretely, in Phase 7:

- Entities, relations, DAOs, converters and `PantryHubDatabase` move to `commonMain`.
- Add `androidx.sqlite:sqlite-bundled` and use `BundledSQLiteDriver` on both platforms, so the
  SQLite build is identical on Android and iOS rather than whatever each OS ships.
- `@ConstructedBy` + an `expect object : RoomDatabaseConstructor`, with the builder in
  `androidMain` (`Room.databaseBuilder(context, dbFile.absolutePath)`) and `iosMain`
  (`Room.databaseBuilder(NSDocumentDirectory + "/pantryhub.db")`).
- Migrations move from `SupportSQLiteDatabase` to `SQLiteConnection`.
- KSP registered per target: `kspAndroid`, `kspIosArm64`, `kspIosX64`, `kspIosSimulatorArm64`.
- `fallbackToDestructiveMigration` must already be gone from release builds (R1 / track C7 of
  `20_Rework_Plan.md`) — this is a hard precondition, not a nice-to-have.

Room KMP does **not** support query callbacks, auto-close, pre-packaged databases,
multi-instance invalidation, `LiveData`/RxJava, or blocking DAO functions off Android. The DAOs
must be audited for non-`suspend`, non-`Flow` functions before the move.

Room 3.0 is a separate, later decision.

## Alternatives
- **SQLDelight.** Rejected: a rewrite of 19 files and both migrations, to replace a library
  that already supports the target platform.
- **Room 3.0 now.** Rejected: bundles a package rename, a type-converter API change and
  suspend migrations into a migration that is already large enough. Two risky changes at once
  makes a failure impossible to attribute.
- **Two separate databases (Room on Android, Core Data on iOS).** Rejected: duplicates the
  schema and the migrations, which is exactly what this plan exists to prevent.

## Consequences

### Positive
- The schema, the DAOs and the migrations exist once.
- `BundledSQLiteDriver` removes "it works on my OS version" from the equation.
- No rewrite of working, data-bearing code.

### Negative
- Migrations must be rewritten against `SQLiteConnection` (R3 — the highest-impact risk in the
  plan; migration tests come first).
- `sqlite-bundled` adds roughly 1–2 MB per ABI.
- The unsupported-API list above must be checked before, not during, the move.

---
Last updated: September 13, 2026
