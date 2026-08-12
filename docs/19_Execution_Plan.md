# PantryHub — Execution Plan (Master Plan)

> **What this document is.** The *single* operational roadmap for the project: what gets
> done, in what order, with what "done" criteria. It supersedes the scattered (and stale)
> status information in the Roadmap, Changelog and Backlog. Whenever you are unsure "what's
> next?", this is the document that decides.
>
> **How to use it.** Each sprint has an objective, a task list and an acceptance criterion.
> Work top to bottom. When a sprint closes, mark it here and add a real entry to the
> Changelog. No "TBD" dates.
>
> Last updated: 2026-08-12

---

## 0. Honest diagnosis (why this plan exists)

The perception ("the app looks very basic, no style, I'm going slow") **does not match the
reality of the repo**. The project is far more advanced than it seems:

**What is ALREADY built and working:**

- Real multi-module Clean Architecture (7 `core-*` + 5 `feature-*` modules), with Hilt,
  Room, Compose, Navigation and kotlinx.serialization wired up and compiling.
- **Shopping**: practically complete — lists, detail, shopping mode, finish purchase with
  price/supermarket, history (`PurchaseEntity`/`PurchaseDao`).
- **Products**: ~90% — CRUD, normalized search, favorites, duplicate detection.
- Full data and domain layers (repositories + use cases, including
  `DetectDuplicateProductUseCase`, `CloneShoppingListUseCase`, `Export/ImportDataUseCase`).
- Structural Design System: theme (Color/Type/Shape/Dimensions), 9 `Pantry*` components,
  bilingual strings (es + en).

**Realistic estimate: ~65-70% of Phase 1 done.**

**Why it feels "basic and styleless":** the design system exists but is **visually generic**
— it uses the Material 3 default color (the purple every tutorial ships with). There is no
custom palette, no typography with character, no motion. `05_Design_System.md` is the
shortest doc of all and only says "use `MaterialTheme.colorScheme`". That, not a lack of
features, is the cause of the "no style" feeling.

**Why it feels "slow":** effort has gone into documentation (18 docs + 11 ADRs, more than
most indie projects have) instead of execution. More documents are not needed; finishing
3-4 features and giving the app a visual identity is.

**Conclusion:** this plan redirects effort from *documenting* to *executing*, in the order
you set: reconcile docs → visual identity → features sprint by sprint → 1.0.

---

## 1. Working agreement (Definition of Done)

Fixed rules for everything from here on. They do not change without an ADR.

**A feature is "done" when:**

1. It compiles with no new warnings and passes Detekt + KtLint.
2. It follows the architecture: no business logic in Composables, no Room in ViewModels,
   everything goes through use case → repository.
3. Zero hardcoded strings. All text lives in `strings.xml` (es + en at minimum).
4. It handles the 3 global states: `PantryLoading`, `PantryEmptyState`, `PantryErrorState`.
5. It has at least: 1 ViewModel test (states) and 1 use-case test if there is new logic.
6. It looks good in light **and** dark, with touch targets ≥ 48dp.
7. A real Changelog entry is added and the sprint is marked in this plan.

**Language rule (fixed):** all code, comments and documentation are written in **English**.
Spanish is used only in direct conversation with the author, never in the repo.

**Product/category naming convention (fixed business rule):** first letter uppercase, rest
lowercase when displayed; `normalized_name` (lowercase, no accents, no extra spaces) for
comparison and duplicate detection.

---

## 2. Code state by module (snapshot)

| Module | State | Note |
|---|---|---|
| `core-model` | ✅ Complete | 7 models + serializable `BackupData` |
| `core-database` | ✅ Complete | 8 entities, 3 DAOs, DB, mappers, converters |
| `core-data` | ✅ Complete | Interfaces + `Offline*` repos + DI |
| `core-domain` | ✅ Complete | product/shopping/backup use cases |
| `core-designsystem` | ⚠️ Structural | components OK, **generic visual identity** |
| `core-navigation` | ✅ OK | destinations + actions |
| `core-common` | ✅ OK | `StringNormalization` |
| `feature-shopping` | ✅ ~100% | real large screens, shopping mode |
| `feature-products` | ✅ ~90% | CRUD, search, favorites, duplicates |
| `feature-importexport` | ⚠️ No UI | logic ready in domain/data, screen missing |
| `feature-notes` | ❌ Empty | gradle scaffolding only |
| `feature-settings` | ❌ Empty | scaffolding only; DataStore declared, unused |
| Categories | ⚠️ Partial | modeled, no management DAO/repo/UI |
| Templates / provisional lists | ⚠️ Partial | `CloneShoppingListUseCase` + enum, no dedicated UI |

> ⚠️ *Caveat*: the internal state of the `.kt` files was mapped from the file tree, sizes
> and dependency graph. Percentages are estimates pending a full code read.

---

## 3. Sprint 0 — Reconcile the documentation

**Objective:** make the docs tell the truth and stop contradicting each other. Fast, and it
unlocks a reliable source of status.

**Tasks:**

- [x] Adopt `STATUS.md` as the single source of truth for progress.
- [x] Flag `17_Backlog.md`: almost everything is `PLANNED` when it is actually `DONE`
      (PB-001, PB-002 catalog, PB-010 theme, PB-011 nav, PB-020 product, PB-021 search,
      PB-022 duplicates, PB-030/031/032 lists, PB-040/041 shopping mode). Point to `STATUS.md`.
- [x] Flag `18_Changelog.md`: remove reliance on the fictional entries with "TBD" dates and
      "Development" states for finished work. Real entries per completed version from now on.
- [x] Fix the `01_Roadmap.md` table: align states with `STATUS.md`.
- [ ] Update `05_Design_System.md` → replaced entirely in Sprint 1.
- [x] Working title: the project is **PantryHub** (the original brief said "PantryFlow";
      now deprecated).

**Acceptance criterion:** open any doc and its state matches `STATUS.md`. Zero
contradictions between Roadmap/Changelog/Backlog/code.

---

## 4. Sprint 1 — Visual identity (the app's "soul")

**Objective:** kill the "looks like a tutorial" feeling. This is the highest impact/effort
sprint.

**Agreed flow:**

1. Generate **2-3 HTML mockups** of the real screens (Lists, Shopping mode, Products) with
   distinct visual identities (palette + typography + spacing + card/state treatment). You
   review them and pick one.
2. With your choice, encode the identity into a real `05_Design_System.md`:
   - Full light/dark palette with semantic roles (primary, surface, container, categories,
     shopping states) and hex values.
   - Typographic scale with character (not the default Roboto).
   - Shapes, elevations, density, and "per-category iconography".
   - Motion principles (item checked and moved down, results appearing, undo).
3. Implement it in `core-designsystem` (`Color.kt`, `Type.kt`, `Shape.kt`, `Theme.kt`) and
   adjust the 9 `Pantry*` components.
4. Re-apply to the existing screens (Shopping + Products) so the jump is visible.

**Tasks:**

- [ ] HTML mockups of 3 styles → selection.
- [ ] Rewrite `05_Design_System.md` (palette, typography, shapes, motion, tokens).
- [ ] Implement theme in Compose (light/dark; dynamic color off by default to keep our own
      identity).
- [ ] Adjust `Pantry*` components to the new identity.
- [ ] Re-apply to Shopping and Products; before/after screenshots.

**Acceptance criterion:** opening the app in light and dark shows a distinct, recognizable
identity; no default M3 purple; before/after screenshots show the jump.

---

## 5. Sprints 2-6 — Complete Phase 1 (road to 1.0)

Order designed to maximize value and unblock dependencies.

### Sprint 2 — Categories (management + discovery)
The missing piece that enables "search by category when I don't remember the name".

- [ ] `CategoryDao` + repo + use cases (create/rename/archive/list).
- [ ] Category management screen + assignment when creating/editing a product.
- [ ] Category browsing ("Vegetables → tomatoes, lettuce…") to rediscover forgotten items.
- [ ] Per-category icon (ties into the Sprint 1 iconography).

**Acceptance:** I can create categories, assign them and browse products by category.

### Sprint 2b — Product aliases (decision closed)
- [ ] Add `aliases` to `ProductEntity` + explicit Room migration (`Migration_X_Y`).
- [ ] Normalize aliases like the name (lowercase, no accents) for search/duplicates.
- [ ] Integrate aliases into `SearchProductsUseCase` ("papa" finds "Patata").
- [ ] UI to manage aliases when editing a product.

**Acceptance:** searching by an alias returns the right product; the migration loses no data.

### Sprint 3 — Import / Export (UI on top of existing logic)
The logic already exists (`Export/ImportDataUseCase`, `BackupRepository`, `BackupData`); the
face is missing.

- [ ] Screen in `feature-importexport`: export → share `.json` (Android share sheet).
- [ ] Import → pick file, **preview** of what comes in, and conflict resolution.
- [ ] Apply similarity detection (Levenshtein/Jaro-Winkler, **threshold 0.8**) on import:
      items identical after normalization (accents/case) merge automatically; similarity
      ≥ 0.8 ("Lenteja"/"Lentejas") is listed for you to decide; < 0.8 ("Cava"/"Caña") = distinct.
- [ ] `schemaVersion` in the JSON + room for future migrations.

**Acceptance:** I export on one phone, import on another and it matches; accent duplicates
merge automatically and ambiguous ones are asked.

### Sprint 4 — Notes
- [ ] `feature-notes`: list + create/edit/delete (with undo).
- [ ] Personal notes; future hook to product/list (fields already in `NoteEntity`).

**Acceptance:** I can create, edit and delete a note; it persists.

### Sprint 5 — Settings
- [ ] `feature-settings` with DataStore: theme (light/dark/system), language, and toggles.
- [ ] In-app language switch (per-app locale) without recompiling.

**Acceptance:** I change theme and language from Settings and it persists across restarts.

### Sprint 6 — Provisional lists / templates + purchase polish
- [ ] `TEMPLATE` and `TEMPORARY` list types with their UI: create from scratch or **clone**
      an existing one.
- [ ] When cloning, apply the same similarity control as import.
- [ ] Polish "finish purchase": "everything in the same store" checkbox → saves supermarket;
      finish with/without price (basis for future analytics).

**Acceptance:** I create a provisional list by cloning another, shop, finish with price and
store, and it lands in history.

---

## 6. Phase 1.0 — Closing and release

- [ ] i18n audit (no hardcoded strings; layouts that hold long text).
- [ ] Accessibility (TalkBack, contrast, dynamic font sizes, focus).
- [ ] Explicit Room migrations (never `fallbackToDestructiveMigration` in release).
- [ ] Test suite (domain + data + ViewModels + 2-3 UI journeys).
- [ ] Play Store assets, icon, release signing, final QA.

**1.0 definition:** products, categories, lists, search, favorites, shopping mode, notes,
import/export, light/dark and languages — all polished and with a custom visual identity.

---

## 7. Code workflow (RESOLVED)

- **Read** the code → clone of the public repo `Shylex96/PantryHub` in the cloud. Verified
  that the local working tree matches GitHub HEAD.
- **Write** changes → written directly into the local folder
  (`AndroidStudioProjects/PantryHub`) so they appear in Android Studio **before** committing.
  Verified that writing `.kt` locally works.

**Sync rule:** before starting a batch of changes, local must be aligned with GitHub (commit
or stash pending work) so the clone is a faithful base. Commits are done by the author after
review; the assistant does not commit or push unless asked.

---

## 8. Decisions (closed / open)

**Closed:**

- **Product aliases:** ✅ YES, in for 1.0. Room migration approved. An alias field is added
  to `ProductEntity` (+ normalization) and integrated into search ("papa"→"patata"). See
  Sprint 2b.
- **Similarity threshold:** ✅ **0.8**. Rule: similarity ≥ 0.8 = duplicate candidate → ask
  the user; below 0.8 = distinct products. Items identical after normalization
  (case/accents/spaces) merge automatically without asking.
- **Language:** ✅ all repo content (code, comments, docs) in English.

**Open (to close when relevant):**

- **Dynamic color (Material You):** off by default to preserve our identity; option in
  Settings to enable it if the user wants.
- **FTS5** for search: not needed for 1.0; noted for when the catalog grows.

---

## 9. Post-1.0 (do not touch until 1.0 ships)

Household units, QR invitations, cloud sync with conflict resolution, and analytics
(spending, store, frequency, price evolution). The architecture is already prepared (UUIDs,
`created_at/updated_at`, soft-delete, offline-first). This will get its own plan once 1.0 is
published.
