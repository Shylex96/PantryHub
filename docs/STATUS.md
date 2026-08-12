# PantryHub — STATUS (single source of truth)

> This document replaces the scattered, stale status that used to live in `01_Roadmap.md`,
> `17_Backlog.md` and `18_Changelog.md`. If another doc contradicts this one, this one wins.
> It is updated when each sprint closes (see `19_Execution_Plan.md`).
>
> Last updated: 2026-08-12

---

## Summary

- **Current phase:** Phase 1 (offline personal app).
- **Phase 1 progress:** ~90%.
- **Infrastructure (architecture, DI, DB, domain, data, navigation):** ~100%.
- **Next:** Settings language (Part B), provisional lists/templates (Sprint 6), or the
  full visual pass. Done: docs, visual identity, categories, aliases, Import/Export,
  Notes, Settings theme (light/dark/system + dynamic color).

---

## Real state by area

| Area | State | Reality |
|---|---|---|
| Multi-module architecture | ✅ Done | 7 core + 5 feature, compiles |
| Hilt / DI | ✅ Done | wired across all modules |
| Room + entities + DAOs | ✅ Done | 8 entities, 3 DAOs, mappers |
| Repositories (offline) | ✅ Done | interfaces + impl + DI |
| Use cases (product/shopping/backup) | ✅ Done | incl. duplicates, clone, export/import |
| Navigation | ✅ Done | graph + bottom nav (lists/products/notes/settings) |
| Design System (structure) | ✅ Done | 9 components + theme |
| **Design System (visual identity)** | ❌ Pending | generic M3 → Sprint 1 |
| Feature: Shopping | ✅ ~100% | lists, detail, shopping mode, finish |
| Feature: Products | ✅ ~90% | CRUD, search, favorites, duplicates |
| Feature: Import/Export | ✅ Done | Sprint 3 (export/import JSON, incl. categories) |
| Categories (management/browse) | ✅ Done | Sprint 2 |
| Product aliases | ✅ Done | Sprint 2b (Room migration v2→v3) |
| Feature: Notes | ✅ Done | Sprint 4 (list + create/edit/delete, in backup) |
| Feature: Settings | ✅ Theme done | Sprint 5A (theme + dynamic color); language = Part B |
| Templates / provisional lists | ⚠️ Partial | Sprint 6 |
| i18n (es + en) | ✅ Base done | audit at 1.0 close |
| Tests | ⚠️ Partial | expand per feature |

Legend: ✅ done · ⚠️ partial · ❌ not started

---

## Milestones (corrected)

| Version | Description | Real state |
|---|---|---|
| v0.1 Foundation | architecture, DI, nav, docs | ✅ Done |
| v0.2 Domain | models + use cases | ✅ Done |
| v0.3 Storage | Room + repos | ✅ Done |
| v0.4 Shopping flow | lists + detail + shopping mode | ✅ Done |
| v0.5 Persistence | reactive flows, no mocks | ✅ Done |
| v0.5.1 Design System (structure) | theme + components | ✅ Done |
| v0.7 Products | CRUD, search, favorites | ✅ Done |
| v0.6 Visual identity | custom palette/typography/motion | ✅ Done |
| Categories | management + browsing | ✅ Done |
| Product aliases | field + migration + search | ✅ Done |
| v0.8 Import/Export | export/import JSON | ✅ Done (incl. import preview + similarity conflicts) |
| Notes | create/edit/delete | ✅ Done |
| Settings | theme via DataStore | ✅ Done (language pending, Part B) |
| Templates / provisional | clone + similarity | 🔜 Sprint 6 |
| v1.0 | close + QA + release | 🔜 |

> Note: the original Roadmap's version numbering did not match the real order of work. The
> sprint order in `19_Execution_Plan.md` is authoritative.
