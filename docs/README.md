# PantryHub Documentation

This folder contains the official documentation of the PantryHub project.

The purpose of this documentation is to provide a single source of truth for product decisions, architecture, design, technical implementation and future development.

Any developer, contributor or AI assistant working on this project should review this documentation before making important changes.

---

# Documentation Purpose

The documentation exists to ensure that PantryHub remains:

- Consistent.
- Maintainable.
- Scalable.
- Understandable over time.

The objective is that a new developer can understand the project vision, architecture and decisions without requiring previous context.

---

# Where to Start

- **`STATUS.md` — the single source of truth for project progress.** If any other document (Roadmap, Backlog, Changelog, Execution Plan) contradicts it, `STATUS.md` wins.
- `20_Rework_Plan.md` — the approved plan from the current state to 1.0 (offline) and the deferred connected phases.
- `19_Execution_Plan.md` — sprint history with "done" criteria (checkbox state is historical).

---

# Document Index

All documents exist and are maintained. Numbers are stable; new documents are appended.

| Document | Description |
|---|---|
| `STATUS.md` | **Single source of truth** for progress: real state by area, visual pass, rework phases, milestones. |
| `00_Vision.md` | Why PantryHub exists, the problems it solves and the long-term direction. |
| `01_Roadmap.md` | Version roadmap: 1.0 offline personal app, 1.1 accounts + sync, 1.2 households/QR, later ideas. |
| `02_Product_Principles.md` | Product rules that every design and implementation decision must follow. |
| `03_User_Flows.md` | Main user journeys (lists, products, shopping mode, notes, import/export, settings). |
| `04_UX_Guidelines.md` | Interaction principles, navigation, feedback, accessibility and copy guidelines. |
| `05_Design_System.md` | Visual language: tokens, color, typography, shape, motion and components in `:core-designsystem`. |
| `06_Technology.md` | Technology stack, versions, build system and the real module layout. |
| `07_Architecture.md` | Clean Architecture + MVVM layering, module dependency rules, state and DI patterns. |
| `08_Domain_Model.md` | Business entities, relationships and invariants (products, categories, lists, notes). |
| `09_Database.md` | Room schema, current version and migrations, indexing and migration policy. |
| `10_API.md` | Future REST API design for the connected phases (1.1+). Not implemented. |
| `11_Import_Export.md` | JSON backup format, import preview, conflict handling and compatibility rules. |
| `12_Synchronization.md` | Future multi-device sync architecture (change tracking, soft delete, outbox). Deferred to R4. |
| `13_QR.md` | QR invitation and sharing system for households (1.2). Deferred to R5. |
| `14_Security.md` | Security and privacy principles for local data and future accounts. |
| `15_Testing.md` | Testing strategy: unit, migration, Compose and integration tests. |
| `16_Release_Process.md` | Versioning, release checklist, signing and store publication. |
| `17_Backlog.md` | Backlog items (PB-xxx). Status columns are historical; see `STATUS.md`. |
| `18_Changelog.md` | Version history. Entries before 2026-08 are unreliable; see the note at its top. |
| `19_Execution_Plan.md` | Sprint-by-sprint execution plan (Sprints 0–6 completed) with "done" criteria. |
| `20_Rework_Plan.md` | Approved rework plan: phases R1–R3 (ship 1.0 offline), R4/R5 deferred (sync, households). |

---

# Documentation Structure

The documentation is organized by areas of responsibility.

---

# Product Documentation

Documents related to the product vision, functionality and user experience flows.

```text
00_Vision.md
01_Roadmap.md
02_Product_Principles.md
03_User_Flows.md
```

## Purpose

These documents define:

- Why PantryHub exists.
- Which problems it solves.
- Which features are planned.
- How users interact with the application.

---

# Design Documentation

Documents related to the visual system and user experience.

```text
04_UX_Guidelines.md
05_Design_System.md
```

## Purpose

These documents define:

- UI principles.
- Visual identity.
- Components.
- Interaction patterns.
- Accessibility requirements.
- Design consistency.

---

# Technical Documentation

Documents related to architecture and engineering decisions.

```text
06_Technology.md
07_Architecture.md
08_Domain_Model.md
09_Database.md
10_API.md
```

## Purpose

These documents define:

- Technology choices.
- Application structure.
- Domain entities.
- Data persistence.
- External communication.

---

# Feature Documentation

Documents related to specific complex features.

```text
11_Import_Export.md
12_Synchronization.md
13_QR.md
14_Security.md
```

## Purpose

These documents define:

- Data exchange.
- Future collaboration.
- User invitations.
- Security requirements.

---

# Quality Documentation

Documents related to testing, releases and project evolution.

```text
15_Testing.md
16_Release_Process.md
17_Backlog.md
18_Changelog.md
```

## Purpose

These documents define:

- Testing strategy.
- Release management.
- Pending work.
- Version history.

---

# Planning and Status

Documents that track what is being done, in what order, and what is actually finished.

```text
STATUS.md
19_Execution_Plan.md
20_Rework_Plan.md
```

## Purpose

- `STATUS.md` is the single source of truth for progress and is updated when each sprint or rework phase closes.
- `19_Execution_Plan.md` records the sprint plan and its "done" criteria (Sprints 0–6 completed).
- `20_Rework_Plan.md` is the approved plan for the remaining work to 1.0 and the deferred connected phases.

---

# Architecture Decisions

Technical decisions that affect the project lifecycle are stored inside:

```text
decisions/
```

Each decision follows the ADR (Architecture Decision Record) format.

Structure:

```text
decisions/

├── ADR-001-Compose.md
├── ADR-002-Room.md
├── ADR-003-Repository.md
├── ADR-004-Modularization.md
├── ADR-005-OfflineFirst.md
├── ADR-006-Hilt.md
├── ADR-007-VersionCatalog.md
├── ADR-008-CleanArchitecture.md
├── ADR-009-CoroutinesFlow.md
├── ADR-010-TestingStrategy.md
└── ADR-011-Design-System.md
```

Font licenses for the bundled typefaces live in `licenses/`.

Each ADR should contain:

- Context.
- Problem.
- Alternatives considered.
- Decision.
- Consequences.

---

# Documentation Rules

All documentation should follow these principles:

## Explain Decisions

Documentation should explain why something was chosen, not only what was implemented.

Example:

Bad:

"Room is used for the database."

Good:

"Room was selected because it provides compile-time query validation, integrates with Kotlin Coroutines and supports future migration requirements."

---

## Avoid Temporary Details

Documentation should focus on stable concepts.

Avoid documenting:

- Temporary implementation details.
- Experimental code.
- Personal development notes.

---

## Keep Documents Updated

When a major architectural or product decision changes:

- Update the relevant document.
- Create an ADR if necessary.
- Update affected references.

---

# AI Development Guidelines

PantryHub is designed to be developed with assistance from AI tools.

Any AI assistant working on this project should:

- Read the documentation before generating code.
- Respect existing architecture decisions.
- Avoid introducing unnecessary dependencies.
- Avoid changing established patterns without justification.
- Ask for clarification when requirements are ambiguous.

The documentation folder acts as the main project context.

---

# Current Documentation Status

All documents 00–20 are written. The documentation set was reconciled with the code on 2026-09-11 (rework phase R1, see `20_Rework_Plan.md`).

Project progress is **not** tracked here: see `STATUS.md`.

---

# Long-Term Objective

This documentation should allow PantryHub to evolve from a personal shopping application into a scalable household management platform while maintaining a clear technical and product direction.

---
Last updated: September 11, 2026
