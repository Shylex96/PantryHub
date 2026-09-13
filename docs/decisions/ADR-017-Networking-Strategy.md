# ADR-017: No networking layer until there is a server

## Status
Proposed — 2026-09-13

## Context
The migration brief devotes a phase to "migrate networking": locate Retrofit usage, assess
whether to move to Ktor, and so on.

The audit found that **Retrofit, OkHttp and Coil appear in `libs.versions.toml` and in
`app/build.gradle.kts`, and in zero source files.** WorkManager likewise: declared in `app`,
used nowhere. There is no `RemoteDataSource`, no API client, no interceptor, no auth and no
sync manager. `20_Rework_Plan.md` already records that the server is deferred (R4/R5) pending
the owner's shared infrastructure.

## Decision
**Delete the unused declarations in Phase 1** — Retrofit, OkHttp, Coil and WorkManager, from
the version catalog and from the modules that declare them. There is no networking migration
because there is no networking.

When a server does exist (R4), the client starts as **Ktor + kotlinx.serialization in
`commonMain`**, not Retrofit:

- Retrofit is JVM/Android-only; adding it now would create the exact Android-specific layer in
  shared code that this plan is removing everywhere else.
- kotlinx.serialization is already in use for backup JSON, so the serialization model carries
  over unchanged.
- The repository interfaces are already the seam: a `RemoteDataSource` plugs in behind them
  without any caller changing.

Image loading, if it is ever needed, stays per-platform (Coil on Android, `AsyncImage` on
iOS). Background work stays per-platform behind a shared contract: WorkManager on Android,
`BGTaskScheduler` on iOS, with the shared code owning only the logic being scheduled.

## Alternatives
- **Add Ktor now, ahead of the server.** Rejected: a client with nothing to talk to, which
  would be rewritten by the time the API exists.
- **Keep the unused declarations "just in case".** Rejected: they inflate the APK and the
  dependency-update surface, and they make the codebase look like it has a network layer.

## Consequences

### Positive
- Three sections of the original migration brief disappear.
- A smaller APK and a smaller dependency-update surface today.
- When the API arrives it is written once, for both platforms.

### Negative
- The Ktor decision is deferred, so its risk is deferred rather than removed.
- Anyone reading the old brief will look for a networking phase that no longer exists — hence
  this ADR.

---
Last updated: September 13, 2026
