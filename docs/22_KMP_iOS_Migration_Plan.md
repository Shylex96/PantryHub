# PantryHub — Kotlin Multiplatform / iOS migration plan

> **Audit + plan. No structural change has been made yet.** This document is the deliverable
> asked for before any migration work starts: what the code really looks like, where it can
> go, what has to change, in what order, and what it costs.
>
> Companion to `07_Architecture.md` (current architecture), `20_Rework_Plan.md` (R1–R5) and
> `STATUS.md` (state of play). Decisions are recorded as ADR-012 … ADR-018.
>
> Date: 2026-09-13 · State: **awaiting approval**

---

## 0. Executive summary

PantryHub is in unusually good shape for this migration, and the plan is smaller than the
brief assumed — but it is blocked on something the brief did not anticipate.

**The good news.**

- `core-model` is **already multiplatform-clean**: 10 files, zero Android, JVM or Java
  imports. It can move to `commonMain` essentially untouched.
- `core-domain` does **not** depend on Hilt. All 36 files import exactly one non-Kotlin
  symbol — `javax.inject.Inject` — plus `java.util.UUID` in five of them. There is no
  `dagger.*` import anywhere in the domain, no `Context`, no `ViewModel`, no `Application`.
- **There is no networking layer to migrate.** Retrofit, OkHttp and Coil appear in
  `libs.versions.toml` and in `app/build.gradle.kts`, and in **zero** source files.
  WorkManager likewise. Phase 7 of the brief ("Networking") is a dependency cleanup, not a
  migration.
- Room is **2.8.4**, which is already KMP-capable, and every entity/DAO/converter uses only
  `androidx.room.*` annotations that carry over unchanged.
- Every entity already has a UUID string id, and the repository boundary is a clean set of
  interfaces bound to `Offline*` implementations.

**The blocker.** The project is on **Kotlin 2.0.21** and **KSP 2.0.21-1.0.28** (October 2024)
while running **AGP 9.3.1** and **Gradle 9.6.1** (2026). Under AGP 9, a KMP library module
must use the `com.android.kotlin.multiplatform.library` plugin, and that path wants KGP ≥ 2.0
with 2.3+ recommended and **KSP ≥ 2.3.1**. Room, Hilt, Compose, coroutines, serialization and
datetime are all on versions from the same 2024 vintage. **Phase 1 is a toolchain upgrade, and
it has to land and ship before a single source set moves.**

**The one structural defect.** `07_Architecture.md` says the repository *interfaces* live in
`core-domain`. They do not — they are in `core-data`, and `core-domain` depends on
`core-data`, which depends on `core-database` (Room) and DataStore. So today **the domain
transitively depends on Room**. Nothing can compile for iOS until that arrow is flipped. This
is worth doing for Android alone, independently of any iOS plan.

**Recommended shape**: convert the existing modules in place (option A), plus one thin
umbrella module whose only job is to assemble the iOS framework. See §5.

**Realistic cost**: 27–41 working sessions, of which 5–8 are the toolchain upgrade and 4–6 are
the architectural fix that pays for itself on Android. **A Mac is required from Phase 4
onward** — and the cheapest way to get iOS compile feedback while developing on Windows is a
GitHub Actions `macos-latest` job, added in Phase 3.

---

## 1. Audit — module by module

Method: every `.kt` file under `core-*` was scanned for imports of `android.*`, `androidx.*`,
`java.*`, `javax.*`, `dagger.*` and `kotlinx.coroutines.android`, plus `Context`,
`Application`, `Activity`, `ViewModel`, `SavedStateHandle`, `WorkManager`, `Parcelable`,
`BuildConfig`, `Log` and `Uri`.

| Module | Files | KMP-ready | Android dependencies (real, in code) | iOS strategy | Priority |
|---|---|---|---|---|---|
| `core-model` | 10 | 🟢 **GREEN** | none | `commonMain` verbatim | **1** |
| `core-common` | 2 | 🟡 YELLOW | `java.text.Normalizer`, `java.util.Locale` (1 of 2 files) | `commonMain` + `expect/actual` for accent folding | **2** |
| `core-domain` | 36 | 🟡 YELLOW | `javax.inject.Inject` (36), `java.util.UUID` (5) | `commonMain`; drop `@Inject`, `kotlin.uuid.Uuid` | **3** |
| `core-data` | 15 | 🟡 YELLOW | 13 clean/`@Inject`-only; `DataModule.kt` (Hilt) + `DataStoreSettingsRepository.kt` (`Context`, DataStore, Hilt qualifier) are RED | interfaces + `Offline*` → `commonMain`; DI + preferences → `androidMain`/`iosMain` | **4** |
| `core-database` | 19 | 🟡 YELLOW | 18 files are pure `androidx.room.*`; `DatabaseModule.kt` is RED (`Context`, `ApplicationInfo`, `SupportSQLiteDatabase`, Hilt) | entities/DAOs/DB → `commonMain` on Room KMP; builder → `expect/actual` | **5** |
| `core-navigation` | 2 | 🔴 RED | `NavHostController`, `NavGraph` | stays Android; iOS uses `NavigationStack` | — |
| `core-designsystem` | 29 | 🔴 RED | Compose / Material 3 throughout | stays Android; iOS gets its own SwiftUI system | — |
| `feature-*` (5) | 32 | 🔴 RED | Compose, Hilt, Navigation Compose | stay Android; iOS builds its own screens on the shared layer | — |
| `app` | 6 | 🔴 RED | Android application | stays Android | — |

### 1.1 File-level detail for the YELLOW modules

**`core-common` (2 files)**

| File | Class | Note |
|---|---|---|
| `util/Similarity.kt` | 🟢 GREEN | pure Kotlin (Jaro-Winkler). Moves as-is. |
| `util/StringNormalization.kt` | 🟡 YELLOW | `java.text.Normalizer` + `java.util.Locale`. **The single genuinely hard file in the whole migration** — see §3.4. |

**`core-domain` (36 files)** — every file is 🟡 YELLOW for the same reason and no other:
`javax.inject.Inject`, which does not exist on Kotlin/Native. Five shopping use cases
(`AddProductToShoppingList`, `CloneShoppingList`, `CreateShoppingList`, `FinishShopping`,
plus `ShoppingUseCases`) also use `java.util.UUID`. Nothing else. No business rule needs
rewriting.

**`core-data` (15 files)**

| Group | Files | Class |
|---|---|---|
| Repository **interfaces** (`ProductRepository`, `CategoryRepository`, `ShoppingListRepository`, `PurchaseRepository`, `NoteRepository`, `BackupRepository`, `SettingsRepository`) | 7 | 🟢 GREEN — only `kotlinx.coroutines.flow.Flow` and `core-model` |
| `Offline*Repository` implementations | 6 | 🟡 YELLOW — `javax.inject.Inject` only |
| `di/DataModule.kt` | 1 | 🔴 RED — Dagger/Hilt |
| `repository/DataStoreSettingsRepository.kt` | 1 | 🔴 RED — `android.content.Context`, `androidx.datastore.preferences.*`, `@ApplicationContext` |

**`core-database` (19 files)**

| Group | Files | Class |
|---|---|---|
| Entities, relations, DAOs, `PantryHubDatabase`, converters | 18 | 🟡 YELLOW — `androidx.room.*` only; carries over to Room KMP unchanged |
| `mapper/Mappers.kt` | (of the 18) | 🟢 GREEN — no imports at all |
| `di/DatabaseModule.kt` | 1 | 🔴 RED — `Context`, `ApplicationInfo`, `SupportSQLiteDatabase` in migrations, `fallbackToDestructiveMigration`, Hilt |

### 1.2 Dependency-graph defect (blocking)

```
                 documented (07_Architecture.md)        actual (build.gradle.kts)

  core-domain ──▶ core-model, core-common            core-domain ──▶ core-data ──▶ core-database
                  (interfaces live in core-domain)                   (interfaces live in core-data)
```

`core-domain/build.gradle.kts` declares `implementation(project(":core-data"))`, and
`core-data` declares `core-database` (Room) and DataStore. The domain therefore sits on top
of Room. Clean Architecture says the arrow points the other way, and **iOS cannot compile the
domain until it does**. Fixing it is Phase 2 and it is valuable with or without iOS.

### 1.3 Declared-but-unused dependencies

Present in `libs.versions.toml` / module `build.gradle.kts`, referenced by **no source file**:

| Dependency | Declared in | Used |
|---|---|---|
| Retrofit 2.11.0 | version catalog | ❌ nowhere |
| OkHttp 4.12.0 | version catalog | ❌ nowhere |
| Coil 2.7.0 | `app`, `core-designsystem` | ❌ nowhere |
| WorkManager 2.10.0 | `app` | ❌ nowhere |

They inflate the APK and the dependency-update surface for nothing. Removing them is a
5-minute change in Phase 1 and it deletes three whole sections of the original brief.

---

## 2. Target architecture

```
                         ┌──────────────┐          ┌──────────────┐
                         │  Android app │          │   iOS app    │
                         │   (:app)     │          │  (iosApp/)   │
                         │  Compose M3  │          │   SwiftUI    │
                         │  Hilt · Nav  │          │ NavigationStack│
                         └──────┬───────┘          └──────┬───────┘
       :core-designsystem ──────┤                         │  PantryHubShared.xcframework
       :core-navigation   ──────┤                         │
       :feature-* (5)     ──────┤                         │
                                └───────────┬─────────────┘
                                            │
                                  ┌─────────▼──────────┐
                                  │   :shared-ios      │  umbrella, iOS only:
                                  │   (framework only) │  exports the four below
                                  └─────────┬──────────┘
                    ┌───────────────┬───────┴────────┬──────────────────┐
              :core-domain    :core-data       :core-database      :core-model
              use cases       repository impls  Room KMP           models
              repo interfaces  + mappers        entities/DAOs      serialization
                    └───────────────┴────────────────┴──────────────────┘
                                      :core-common
                                 normalization · similarity

   commonMain  · everything above the app row
   androidMain · Hilt bindings, DataStore(Context), Room builder(Context)
   iosMain     · Swift-facing factories, DataStore(Okio/NSDocumentDirectory), Room builder(path)
```

**What is shared**: models, serialization, use cases, repository contracts *and* their offline
implementations, entity↔model mappers, the Room schema, validation, duplicate detection,
shopping calculations, import/export. That is roughly **82 of 151 files (54%)** — and it is
the half that holds every business rule.

**What is not**: UI, navigation, dependency injection wiring, background work, image loading.
Per platform, by design (ADR-013).

---

## 3. Dependency changes

### 3.1 Toolchain (Phase 1 — blocking prerequisite)

| | Now | Target | Why |
|---|---|---|---|
| Kotlin | **2.0.21** | **2.3.x** (2.4.20 is current) | AGP 9 + KMP wants ≥ 2.0, recommends ≥ 2.3. 2.0.21 predates AGP 9 entirely. |
| KSP | **2.0.21-1.0.28** | **≥ 2.3.1** | Minimum for the AGP 9 KMP path. Since Kotlin 2.3, KSP versioning is decoupled from the compiler. |
| AGP | 9.3.1 | 9.3.1 ✅ | already fine |
| Gradle | 9.6.1 | 9.6.1 ✅ | ≥ 9.1.0 required |
| JDK toolchain | 21 | 21 ✅ | ≥ 17 required |
| Compose compiler | tracks Kotlin | tracks Kotlin | moves with the Kotlin bump |
| Hilt | 2.60.1 | latest compatible with the chosen Kotlin | Android side only |
| coroutines | 1.9.0 | ≥ 1.10 | `Dispatchers.IO` on Native landed in 1.9; stay current |
| serialization | 1.7.3 | current | |
| kotlinx-datetime | 0.6.1 | current | already multiplatform |
| Compose BOM | 2026.02.01 | keep / bump with Kotlin | |

Do **not** combine this with any source-set move. It is its own branch, its own release, its
own regression pass.

### 3.2 Per-library verdict

| Library | Verdict | Action |
|---|---|---|
| Kotlin stdlib, coroutines, serialization, datetime | 🟢 share | none |
| **Room 2.8.4** | 🟢 share, **keep it** | 2.8+ is KMP-capable. Add `androidx.sqlite:sqlite-bundled` + `BundledSQLiteDriver`; move migrations off `SupportSQLiteDatabase`. See ADR-016. |
| Room 3.0 (`androidx.room3`) | 🟡 later, separately | Released March 2026: new package, `@ColumnTypeConverter`, suspend migrations. Google's own guidance is two-phase — modernise on 2.8 first. Not part of this plan. |
| **DataStore 1.1.1** | 🟡 adapt | DataStore **Preferences** supports KMP from 1.1.0; bump to 1.2.x, use `datastore-core` + `datastore-preferences-core` in `commonMain`, `FileStorage(context.filesDir)` on Android and `OkioStorage(NSDocumentDirectory)` on iOS. Proto DataStore is not supported on KMP — irrelevant here. |
| **Hilt** | 🔴 Android only | Never enters `commonMain`. See ADR-015. |
| `javax.inject.Inject` | 🔴 remove from shared | Not available on Native. 36 annotations to delete. |
| `java.util.UUID` | 🔴 replace | `kotlin.uuid.Uuid` (stdlib; may still need `@OptIn(ExperimentalUuidApi::class)` depending on the Kotlin version). |
| `java.text.Normalizer` | 🔴 `expect/actual` | §3.4 |
| **Retrofit / OkHttp** | ⚪ delete | unused (§1.3). If a network layer is ever needed, it starts as Ktor in `commonMain` — ADR-017. |
| **Coil / WorkManager** | ⚪ delete | unused. Background work on iOS would be `BGTaskScheduler`, behind a shared contract. |
| Compose / Material 3 / Navigation Compose | 🔴 Android only | ADR-013 |

### 3.3 AGP 9 changes every shared module's build file

This is not optional and it is easy to get wrong. For each module that becomes KMP:

```kotlin
// BEFORE
plugins { alias(libs.plugins.androidLibrary) }
android {
    namespace = "com.pantryhub.core.model"
    compileSdk = 35
    defaultConfig { minSdk = 28 }
}

// AFTER
plugins { alias(libs.plugins.androidMultiplatformLibrary) }   // com.android.kotlin.multiplatform.library
kotlin {
    androidLibrary {
        namespace = "com.pantryhub.core.model"
        compileSdk = 35
        minSdk = 28
        compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
    }
    iosX64(); iosArm64(); iosSimulatorArm64()
    sourceSets {
        commonMain.dependencies { /* … */ }
    }
}
```

Gotchas, in the order they will bite:

1. The `android { }` block is **replaced**, not kept alongside. Leaving it is a hard failure.
2. `com.android.library` is **not compatible** with KMP under AGP 9 — the plugin swap is
   mandatory, not cosmetic.
3. KMP library modules have **no build variants**: `debugImplementation` becomes
   `androidRuntimeClasspath`. `core-designsystem` and `feature-settings` use
   `debugImplementation(libs.androidx.ui.tooling)` — they stay Android-only, so they are not
   affected, but do not copy the pattern into a shared module.
4. `gradle.properties` currently sets `android.disallowKotlinSourceSets=false` — a legacy
   escape hatch. Revisit it once the modules are real KMP modules.
5. `android.enableLegacyVariantApi=true` exists as a temporary crutch and is **removed in AGP
   10** (expected H2 2026). Do not lean on it.
6. KSP must be registered per target: `kspAndroid`, `kspIosArm64`, `kspIosX64`,
   `kspIosSimulatorArm64` — not a bare `ksp(...)`.

### 3.4 The one hard file

`core-common/util/StringNormalization.kt` strips accents with `java.text.Normalizer` and
lowercases with `java.util.Locale`. It has no common equivalent, and it is **load-bearing**:
`normalized_name` and `normalized_aliases` in the database, duplicate detection, and the whole
product search path go through it. If Android and iOS normalise differently, the *same
database* yields different search results and different duplicate warnings on the two devices.

```kotlin
// commonMain
expect fun String.foldAccents(): String

// androidMain
actual fun String.foldAccents(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace(Regex("\\p{Mn}+"), "")

// iosMain — NSString folding, NOT a hand-rolled character table
actual fun String.foldAccents(): String =
    (this as NSString).stringByFoldingWithOptions(
        NSDiacriticInsensitiveSearch or NSWidthInsensitiveSearch,
        locale = null
    )
```

Then lowercase with Kotlin's locale-invariant `lowercase()` rather than `Locale`-dependent
JVM behaviour — note this may itself change today's Android results for Turkish-style edge
cases, so it needs a regression check.

**Non-negotiable**: a `commonTest` suite with a shared fixture table (`Melón/melon`,
`Jamón/jamon`, `Café/cafe`, `Ñoquis/noquis`, `ÅÄÖ`, `ß`, emoji, empty) that runs on **both**
targets and asserts byte-identical output. Write these tests *before* the `expect/actual`
split, against the current Android implementation, so they capture today's behaviour.

---

## 4. Risks

| # | Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|---|
| R1 | The Kotlin 2.0→2.3 jump breaks Hilt/KSP/Compose in ways unrelated to iOS | **high** | high | Its own phase, its own branch, its own release. Bump one thing at a time; keep the build green after each. Do not start KMP work until it ships. |
| R2 | Accent folding diverges between platforms → different search/duplicate results on the same data | medium | **high** | `commonTest` fixture table written first (§3.4); treat any divergence as a release blocker. |
| R3 | Room KMP migration loses data | low | **critical** | `fallbackToDestructiveMigration` must already be gone (R1/C7). `MigrationTestHelper` for 2→3→4 **before** touching the schema. Migrations must move from `SupportSQLiteDatabase` to `SQLiteConnection`. |
| R4 | Cannot verify iOS from Windows — Kotlin/Native iOS targets do not build on a Windows host | **certain** | high | GitHub Actions `macos-latest` job from Phase 3, running `assemble` for the iOS targets on every push. Cheap, and it turns "I hope it compiles" into a red/green signal. |
| R5 | Removing 36 `@Inject` annotations breaks Hilt's Android graph | medium | medium | Keep `@Inject` on *Android* bindings; provide shared use cases from a Hilt `@Module` in `androidMain` or in `:app`. The domain classes become plain constructors. |
| R6 | The `core-domain → core-data` flip touches every use case | medium | medium | Pure move + import rewrite, no logic change. Do it as its own commit, Android-only, and ship it before any KMP work — it stands on its own merit. |
| R7 | Effort creep: "while we're here" rewrites | medium | medium | The brief's own rule: no Compose Multiplatform, no Room replacement, no networking rewrite. Anything not in §5 is out of scope. |
| R8 | Room KMP's unsupported surface bites late | low | medium | Room KMP has no query callbacks, no auto-close, no pre-packaged DB, no multi-instance invalidation, no `LiveData`/Rx, and **no blocking DAO functions off Android**. Audit the DAOs for blocking (non-`suspend`, non-`Flow`) functions in Phase 5. |
| R9 | iOS release work (signing, App Store, TestFlight) is invisible in this plan | **certain** | medium | It is genuinely out of scope here; budget a separate phase equivalent to R3 on the Android side. |
| R10 | AGP 10 removes the legacy variant API mid-migration | low | medium | Do not enable `enableLegacyVariantApi`; migrate module build files properly the first time. |

---

## 5. Migration order

Phase 0–3 run entirely on Windows and are all valuable **without iOS**. The Mac is needed from
Phase 4.

| Phase | Name | Scope | Mac? | Sessions |
|---|---|---|---|---|
| **0** | Safety | branch + tag, confirm clean tree, confirm `assembleDebug` + tests green, capture a baseline APK | no | 0.5 |
| **1** | Toolchain | Kotlin → 2.3.x, KSP → 2.3.x, coroutines/serialization/datetime/Hilt current; **delete Retrofit, OkHttp, Coil, WorkManager**; full Android regression; **ship it** | no | 5–8 |
| **2** | Dependency inversion | move the 7 repository interfaces `core-data` → `core-domain`; `core-domain` drops `core-data`; `core-data` gains `core-domain`; update `07_Architecture.md` to match reality | no | 4–6 |
| **3** | `core-model` → KMP | `com.android.kotlin.multiplatform.library`, `commonMain`/`commonTest`/`androidMain`/`iosMain`, iOS targets; **add the macOS CI job** | no | 2–3 |
| **4** | `core-common` → KMP | `Similarity` verbatim; `expect/actual` accent folding with the fixture suite (§3.4) | **yes** | 2–3 |
| **5** | `core-domain` → KMP | drop 36 `@Inject`, `java.util.UUID` → `kotlin.uuid.Uuid`; port the use-case tests to `commonTest`; Hilt provides the use cases from the Android side | yes | 4–5 |
| **6** | DI seam | Android keeps Hilt in `:app`; shared code exposes plain constructors + a small factory object Swift can call. Domain never sees a DI framework (ADR-015) | yes | 2–3 |
| **7** | `core-database` → Room KMP | entities/DAOs/DB to `commonMain`; `BundledSQLiteDriver`; `expect/actual` builder; migrations `SupportSQLiteDatabase` → `SQLiteConnection`; migration tests on both platforms; DAO blocking-function audit | yes | 5–7 |
| **8** | `core-data` → KMP | `Offline*` to `commonMain`; `PreferencesStore` abstraction with DataStore on both sides; `DataModule` stays `androidMain` | yes | 3–4 |
| **9** | `iosApp` + `:shared-ios` | Xcode project, umbrella module exporting the four shared modules, XCFramework wired locally (no artifact publishing); **first screen: Shopping Lists**, proving SwiftUI → use case → repository → Room | yes | 4–6 |
| **10** | Feature by feature | List detail → Shopping mode → Products → Notes → Settings → Import/Export, SwiftUI only, zero duplicated business rules | yes | 8–14 |

**Total to a working iOS app with one real screen (Phase 9): ~27–41 sessions.**
Phases 1 and 2 (9–14 of those) improve the Android app on their own and should ship whether or
not iOS ever happens.

### 5.1 Option A vs option B — answered

The brief asked whether to convert the existing modules (A) or create a `sharedLogic` module
and migrate into it (B).

**Recommendation: A, plus one thin umbrella.** The existing 7-module split is the best thing
about this codebase; option B collapses it into one module and throws that away. Convert
`core-model`, `core-common`, `core-domain`, `core-data` and `core-database` in place, and add
a single `:shared-ios` module that contains **no code** — it only declares the iOS framework
and `export`s the four shared modules so Swift has one `import PantryHubShared`. That is the
standard multi-module KMP pattern: modularity preserved on the Kotlin side, one framework on
the Swift side. Recorded as ADR-014.

### 5.2 What is explicitly out of scope

Compose Multiplatform (ADR-013), replacing Room (ADR-016), introducing networking
(ADR-017), sharing ViewModels or navigation (ADR-018), Room 3.0, iOS App Store release
mechanics, and anything in R4/R5 of `20_Rework_Plan.md` (server + sync).

---

## 6. Git plan

Branch per phase off `main`, never on `main`. Tag before starting:

```
git tag v0.9-pre-kmp
git checkout -b chore/toolchain-upgrade        # phase 1 → merge and release
git checkout -b refactor/domain-owns-contracts # phase 2 → merge
git checkout -b feat/kmp-core-model            # phase 3
…
```

Commit shape, one per phase step, each independently revertible:

```
chore(build): upgrade Kotlin to 2.3.x and KSP to 2.3.x
chore(build): remove unused Retrofit, OkHttp, Coil and WorkManager
refactor(domain): move repository interfaces from core-data to core-domain
feat(kmp): convert core-model to a multiplatform library
ci: build iOS targets on macOS runners
feat(kmp): convert core-common with expect/actual accent folding
refactor(domain): remove javax.inject from shared code
feat(kmp): convert core-domain to common code
feat(kmp): move Room entities and DAOs to commonMain
refactor(data): split platform-specific preference storage
feat(ios): add the iosApp Xcode project and shared framework
feat(ios): implement the Shopping Lists screen
```

**Every phase must leave `./gradlew assembleDebug` and the Android test suite green.** From
Phase 3, the macOS CI job must also be green. A phase that cannot satisfy both is reverted,
not patched forward.

---

## 7. Testing plan

| Layer | Where | What |
|---|---|---|
| **Normalization + similarity** | `commonTest` (`core-common`) | The §3.4 fixture table, byte-identical on both targets. Written **before** the `expect/actual` split. The highest-value tests in this plan. |
| **Use cases** | `commonTest` (`core-domain`) | clone, finish shopping (incl. one-off deletion), duplicate detection, import remap, shopping calculations. Move the existing Android unit tests here rather than writing new ones. |
| **Serialization** | `commonTest` (`core-model`) | Backup JSON round-trip, both platforms, same bytes. Guards the export-on-Android / import-on-iOS path. |
| **Mappers** | `commonTest` (`core-data`) | entity ↔ model, both directions |
| **Room migrations** | `androidUnitTest` + iOS instrumented | `MigrationTestHelper` for 2→3→4 **before** Phase 7 touches anything; then the same schema opening cleanly on iOS |
| **Repositories** | `androidUnitTest` / iOS test | in-memory Room on each platform |
| **Android UI** | `androidTest` | unchanged; the existing Compose smoke tests must keep passing through every phase |
| **iOS UI** | XCTest | from Phase 9, one journey per migrated feature |

The rule from `15_Testing.md` still holds and gets easier: **critical logic is tested once, in
shared code.** Today the test suite is thin (`STATUS.md`: "Tests ⚠️ Partial"). Phases 4, 5 and
7 are the natural moment to fix that, because a shared test is worth twice what an
Android-only one is.

---

## 8. Decisions to record

| ADR | Title |
|---|---|
| ADR-012 | Kotlin Multiplatform adoption strategy |
| ADR-013 | Native SwiftUI over Compose Multiplatform |
| ADR-014 | Convert modules in place + one iOS umbrella (A over B) |
| ADR-015 | Dependency injection for KMP: constructors in shared code, Hilt only on Android |
| ADR-016 | Keep Room, migrate to Room KMP on 2.8 |
| ADR-017 | No networking layer until there is a server |
| ADR-018 | Navigation, design system and ViewModels stay per-platform |

---

## 9. Success criteria

Unchanged from the brief, with the toolchain and inversion work made explicit:

1. Android compiles, runs and its tests pass **after every phase**.
2. Kotlin/KSP are current and the unused dependencies are gone.
3. `core-domain` depends on `core-model` and `core-common` only — never on `core-data`,
   Room or DataStore.
4. Models, use cases, repository contracts and their offline implementations live in
   `commonMain`.
5. The shared code compiles for `iosArm64`, `iosX64` and `iosSimulatorArm64` in CI.
6. Accent folding and similarity produce identical results on both platforms, proven by
   `commonTest`.
7. `iosApp` exists, imports one framework, and its first screen runs a real use case against
   a real Room database.
8. No business rule exists twice.
9. Android and iOS can be developed in parallel without either blocking the other.

---

## 10. What is needed to start

1. **Approval of this plan** (and of ADR-012 … ADR-018).
2. **A decision on the Kotlin target**: 2.3.x (conservative, meets the AGP 9 recommendation)
   or 2.4.20 (current, K1 removed entirely). Phase 1 depends on it.
3. **A Mac, or a macOS CI runner**, before Phase 4. GitHub Actions `macos-latest` is enough
   for compile verification; a physical Mac is required for the simulator, debugging and
   anything in Phase 9 onward.
4. A confirmation that R3 ("Ship 1.0 offline") comes **first**. `20_Rework_Plan.md` has 1.0
   as the next milestone; this plan assumes iOS starts after it ships, and that Phase 1 is the
   bridge between the two.
