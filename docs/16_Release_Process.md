# PantryHub Release Process

## Overview

This document defines the release process for PantryHub.

The goal is to establish a predictable workflow for:

- Development.
- Testing.
- Versioning.
- Publishing.
- Maintenance.

A release should represent a stable and understandable state of the application.

---

# Release Philosophy

A release is not only a new version number.

A release means:

- The application is stable.
- The main flows work correctly.
- Known critical issues are resolved.
- Documentation is updated.

---

# Versioning Strategy

PantryHub follows:

```
Semantic Versioning
```

Format:

```
MAJOR.MINOR.PATCH
```

Example:

```
1.2.3
```

---

# Version Meaning

## MAJOR

Used for breaking changes.

Example:

```
1.x.x

↓

2.x.x
```

Possible cases:

- Complete architecture changes.
- Breaking data migrations.
- Major product redesign.

---

## MINOR

Used for new features.

Example:

```
1.1.0
```

Examples:

- New shopping features.
- New screens.
- New integrations.

---

## PATCH

Used for fixes.

Example:

```
1.1.1
```

Examples:

- Bug fixes.
- Performance improvements.
Small UI corrections.

---

# Development Versions

Before 1.0:

```
0.x.x
```

Example:

```
0.5.0
```

Meaning:

The product is still evolving.

---

# Version 1.0

Version:

```
1.0.0
```

represents the first stable public release.

Minimum requirements:

- Core shopping workflow completed.
- Import/export available.
- Stable database.
- Good user experience.
- Testing completed.

---

# Git Strategy

Recommended workflow:

```
main

|

develop

|

feature branches
```

---

# Main Branch

Purpose:

Production-ready code.

Rules:

- Always buildable.
- Stable.
- Tagged releases only.

---

# Develop Branch

Purpose:

Integration branch.

Contains:

- Completed features.
- Tested changes.
- Upcoming release work.

---

# Feature Branches

Naming:

```
feature/name
```

Examples:

```
feature/product-search

feature/import-export

feature/dark-mode
```

---

# Bug Fix Branches

Naming:

```
fix/name
```

Examples:

```
fix/search-crash

fix/database-migration
```

---

# Release Branches

Created before publishing.

Example:

```
release/1.0.0
```

Purpose:

- Final testing.
- Bug fixes.
- Documentation updates.

---

# Hotfix Branches

For urgent production issues.

Example:

```
hotfix/1.0.1
```

---

# Commit Convention

Commits should follow:

```
type(scope): description
```

---

Examples:

Feature:

```
feat(products): add product search
```

Fix:

```
fix(database): repair migration
```

Documentation:

```
docs(readme): update architecture notes
```

---

# Commit Types

Supported:

```
feat

fix

docs

refactor

test

build

chore
```

---

# Pull Requests

Every significant change should be reviewed.

A pull request should contain:

- Description.
- Reason for change.
- Testing performed.
- Screenshots if UI changes.

---

# Release Workflow

Complete flow:

```
Development

↓

Feature complete

↓

Testing

↓

Release branch

↓

Final validation

↓

Tag

↓

Publish
```

---

# Release Checklist

Before creating a release:

## Code Quality

Check:

- Build successful.
- No compilation errors.
- Static analysis passes.

---

## Testing

Verify:

- Unit tests pass.
- Integration tests pass.
- Critical UI flows work.

---

## Database

Check:

- Migrations tested.
- No data loss.
- Import/export compatibility.

---

## UI

Verify:

- Light theme.
- Dark theme.
- Different screen sizes.
- Accessibility.

---

## Localization

Verify:

- All strings translated.
- No missing resources.

---

# Android Build Configuration

The project should have:

## Debug Build

Purpose:

Development.

Characteristics:

- Debug enabled.
- Logging allowed.
- Not distributable.

---

## Release Build

Purpose:

Production.

Characteristics:

- Signed.
- Optimized.
- No sensitive logs.

---

# Application Signing

Production builds require:

```
Signing Key
```

---

## Rules

The signing key must:

- Be backed up securely.
- Never be committed.
- Have restricted access.

---

# Play Store Preparation

Before publishing:

Prepare:

- Application name.
- Description.
- Screenshots.
- Privacy policy.
- Release notes.

---

# Release Notes

Each release should include:

- New features.
- Improvements.
- Fixed issues.

Example:

```
Version 1.1.0

Added:

- Product categories.
- Improved search.

Fixed:

- Shopping mode sorting issue.
```

---

# Changelog Update

Every release updates:

```
docs/17_Changelog.md
```

---

# Database Migration Policy

Every database change requires:

Documentation:

```
Migration number

Purpose

Data impact

Testing
```

---

# Rollback Strategy

If a release has critical issues:

Possible actions:

- Stop rollout.
- Publish hotfix.
- Restore previous version if possible.

---

# Release Channels

Future:

```
Internal Testing

Beta

Production
```

---

# Beta Releases

Before public release:

Recommended:

```
Closed testing
```

with selected users.

Purpose:

Detect:

- UX problems.
- Real usage issues.
- Device-specific bugs.

---

# Feature Flags

Future possibility:

Allow disabling incomplete features.

Example:

```
household_sync_enabled=false
```

---

# Monitoring

After release:

Monitor:

- Crashes.
- Reviews.
- Performance.
- User feedback.

---

# Version 1.0 Release Goal

The first public release should provide:

```
Reliable offline shopping management

+

Personal product memory

+

Easy list creation

+

Safe data portability
```

---

# Final Release Vision

The release process exists to ensure:

```
Fast development

+

Stable software

+

Predictable evolution
```

as PantryHub grows over time.

---
Last updated: July 26, 2026
