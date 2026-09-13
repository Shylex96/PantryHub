# PantryHub — UX agility pass (sheets · toasts · capitalization)

> Part of **R2 › Track B4** (cross-cutting pass) in `20_Rework_Plan.md`.
> Companion to `04_UX_Guidelines.md` (behaviour) and `05_Design_System.md` (tokens).
>
> Date: 2026-09-13 · State: **implemented, pending a build + device pass**

---

## 1. Why

The redesign landed screen by screen and the app now *looks* right, but three things still
make it *feel* slower and less finished than it is:

1. **Sheets feel heavy.** Every creation and edit flow is a `ModalBottomSheet`. The sheet
   settles, then the keyboard opens and pushes it again — two movements where the user
   expects one — and the field is not focused until after both have finished.
2. **Confirmations are loud.** Successful actions were being acknowledged by repainting the
   control that triggered them (a button turning green). That draws the eye to the thing the
   user has already finished with, and it does not survive the sheet closing.
3. **Capitals are not guaranteed.** Names typed into a list, product, category or note title
   frequently started lowercase, despite `KeyboardCapitalization.Sentences` being set.

None of these is a bug report. All three are the difference between "works" and "feels
native", which is what R2 is for.

---

## 2. What was decided

### 2.1 Sheets: one movement, not three

| Change | Where | Why |
|---|---|---|
| `skipPartiallyExpanded = true`, no `confirmValueChange` | `PantrySheet` | The half-expanded stop adds a settle step nobody asked for. The `confirmValueChange = { true }` that was there is the default and did nothing. |
| `tonalElevation = 0.dp` | `PantrySheet` | `containerColor` is already the raised step (`surfaceContainerHigh`); the elevation tint only adds a second surface to blend every frame. |
| `contentWindowInsets = { WindowInsets(0) }` + `imePadding()` + `navigationBarsPadding()` on the content | `PantrySheet` | The sheet stops applying insets of its own and the content applies them once. This is what removes the second jump when the keyboard opens. |
| `rememberSheetFocusRequester()` | `PantrySheet` (new helper) | `LaunchedEffect(Unit) { requestFocus() }` runs before the sheet content is attached: it either throws *FocusRequester is not initialized* or opens the keyboard a beat after the sheet has finished sliding in. The helper waits one frame (`withFrameNanos`) and wraps the call, so the sheet and the keyboard arrive together and a missed attach can never crash a sheet. |

The `ModalBottomSheet` animation spec itself is not configurable in Material 3, so "instant"
here means *removing the extra movements*, not shortening the slide. Everything above is
about the second and third movement, which is where the heaviness actually came from.

### 2.2 Toasts, top-right

A small pill slides in from the right, sits for 2s and leaves. Entrance 200 ms / exit 150 ms
— fast enough not to read as a screen transition (`05_Design_System.md` §7: micro 100,
standard 250).

**One host for the whole app.** `PantryToastHost { … }` wraps the navigation graph in
`App.kt` and provides `LocalPantryToast`; screens do `val toast = LocalPantryToast.current`
and never build their own. This matters for three reasons: a confirmation survives the screen
that triggered it, it is never clipped by a sheet or the bottom bar, and adding one to a
screen costs a line instead of a `Box` wrapper.

**When to use which.** Toasts confirm *successful, completed* actions ("List created",
"Category renamed"). **Deletions keep the snackbar-with-Undo pattern** (`04_UX_Guidelines.md`
"Undo Pattern") — a toast has nothing to tap, so it is the wrong control for a reversible
destructive action. `PantryToastType.Error` / `Info` exist for later; only `Success` is wired
today.

Wired: category added / renamed / deleted · product added / updated · product added to a list
· list created · list renamed · note saved.

The `isSuccess` flag that had been added to `PantryButton` was removed. It was never used by
any caller and it is the pattern toasts replace — confirming an action without repainting the
interface.

### 2.3 Capitalization: stop relying on the keyboard alone

Two separate causes, two separate fixes.

**Cause A — too many hints.** Fields had accumulated `capitalization` *plus* an explicit
`keyboardType = KeyboardType.Text` *plus* `autoCorrectEnabled = true`. Several IMEs drop the
capitalization hint when the options are over-specified. The fix is one shared, minimal
default:

```kotlin
object PantryKeyboard {
    val text = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
}
```

Every field now starts from it, and callers that need an IME action *copy* it
(`PantryKeyboard.text.copy(imeAction = ImeAction.Done)`) instead of building a fresh
`KeyboardOptions` and dropping the capitalization on the way. `KeyboardCapitalization.Words`,
which had been used for names, is gone: it capitalises *every* word, which is not what
"Melón con pipas" should look like.

**Cause B — the hint is only a hint.** `KeyboardCapitalization` is advisory; the IME may
ignore it, and a physical keyboard or paste bypasses it entirely. So name fields also pass
`capitalizeFirstLetter = true`, which uppercases the first character in `onValueChange`. That
is what makes the capital a guarantee rather than a request. It is opt-in and applies only to
*names*: list, product, category, note title. Free text (note body, aliases) keeps the hint
only.

Applied to 9 fields across Notes, Products, Categories, Lists and List detail.

### 2.4 One regression fixed on the way

`PantryTextField(value: String, …)` had been rewritten to delegate to a `TextFieldValue`
overload, building `TextFieldValue(text, selection = TextRange(text.length))` on **every
recomposition**. That forces the caret to the end of the field on every keystroke — editing
anywhere but the end became impossible — and resetting the value also resets the IME's
composing region, which is a third, independent way to lose auto-capitalization.

The `String` overload now goes straight to `OutlinedTextField`. The `TextFieldValue` overload
stays, for the few sheets that legitimately want the caret placed at the end of existing text
when they open (note title, product name, category name, list rename) — there the value is
`remember`ed once, which is the safe use of it.

---

## 3. Files touched

**`:core-designsystem`**

| File | Change |
|---|---|
| `PantryToast.kt` | *new* — `PantryToastState`, `LocalPantryToast`, `PantryToastHost { }`, `PantryToastLayer`. Toast data carries an `id` so two identical consecutive messages still animate; the layer is `statusBarsPadding()`-aware. |
| `PantryAnimations.kt` | *new* — `Modifier.shake(trigger)` for validation feedback. |
| `PantrySheet.kt` | Sheet agility (§2.1) + `rememberSheetFocusRequester()`. |
| `PantryTextField.kt` | `PantryKeyboard.text`, `capitalizeFirstLetter`, `String.capitalizeFirstChar()`, caret regression fixed. |
| `PantryButton.kt` | reverted (`isSuccess` removed). |
| `values/strings.xml`, `values-es/strings.xml` | 8 toast strings, en + es. |

**`:app`** — `App.kt` mounts `PantryToastHost` once, around the `Scaffold`.

**features** — `NotesScreen`, `ProductsScreen`, `ProductSheets`, `CategoriesScreen`,
`ShoppingListsScreen`, `ShoppingListDetailScreen`: composition-local toasts, shared keyboard
defaults, `capitalizeFirstLetter` on name fields, `rememberSheetFocusRequester()`, shake on
empty submit.

---

## 3.1 One screen reverted before reapplying

`ShoppingListDetailScreen.kt` had been rewritten mid-session, not just extended, and the
rewrite **did not compile**:

| Problem | Detail |
|---|---|
| `PantryIcons.More` | no such icon in `PantryIcons` |
| `R.plurals.item_count_subtitle` | no such plural; the screen previously used `R.plurals.products_count` |
| `ShoppingIntent.ToggleFavorite(item.product.id)` | the intent takes `(productId, isFavorite)` |
| `PantrySwipeRow(isFavorite = …)` | no such parameter |
| `groups.forEach { (category, items) -> … }` | `ItemGroup` is `(category, color, items)`; positional destructuring bound `items` to a `Color` |
| `Modifier.animateItem()` inside `SwipeableItemRow` | `animateItem` is a `LazyItemScope` extension and the row is a plain composable |

It had also silently dropped working behaviour: the delete-list action became unreachable
(`showDeleteConfirm` was never set to `true`, so `onDeleteList` was dead code), the
`state.isLoading` branch was gone, the bottom CTA moved out of `bottomBar` into an overlay
with a hardcoded 100dp content inset and rendered even on an empty list, the category dots
disappeared from both the section labels and the rows, and the title lost its
`maxLines`/ellipsis so long list names overflow.

The screen was therefore **reset to the last committed version** and only the agility changes
were reapplied on top: the quick-add toast (now trimming the query, which the old code did
not), the rename toast, `PantryKeyboard.text` + `ImeAction.Done` on both fields,
`capitalizeFirstLetter`, shake-on-empty, `rememberSheetFocusRequester()` in the rename sheet,
and a `TextFieldValue` remembered **once** so the caret opens at the end of the existing name.
Net effect on that file: 87 changed lines instead of 438.

---

## 4. Acceptance criteria

1. Opening any creation sheet is **one** movement: the sheet slides up already focused, with
   the keyboard, and the field sits above it.
2. Typing in the middle of an existing name works — the caret stays where it was put.
3. The first letter of a new list, product, category or note title is uppercase, on Gboard,
   on SwiftKey, with autocorrect off, and when pasted.
4. Creating, renaming or saving shows a pill top-right that disappears on its own; no control
   changes colour to confirm.
5. Doing the same action twice in a row shows the toast twice.
6. Deleting still offers Undo, not a toast.
7. Light (Warm Pantry) and dark (Nocturne) both read correctly, including the toast contrast.
8. On list detail specifically: delete-list still works from the top bar, the loading state
   still shows, the bottom CTA is still hidden on an empty list, and category dots are still
   on the section labels and the rows.

---

## 5. Not done here (left for B4/B5)

- The `Box(Modifier.fillMaxSize())` wrappers added to `CategoriesScreen` and
  `ShoppingListDetailScreen` to host their own toast layer are now redundant. Harmless;
  remove them with the rest of the B4 dead-code sweep.
- `Modifier.shake` uses `composed { }`, which is soft-deprecated in favour of a
  `Modifier.Node`. Fine as is; worth converting when B4 touches the file.
- `PantryToastType.Error` / `Info` have no callers yet. They are the natural home for import
  failures (`feature-importexport`) once that screen gets its B4 pass.
- Undo snackbars for deletions, empty/error state consistency and the light-theme sweep are
  still the rest of B4.
- Accessibility (B5) is untouched: the toast needs a live-region semantics pass so TalkBack
  announces it.

---

## 6. Verification status

**These changes have not been compiled.** They were written against a source snapshot, not in
a build environment. Everything is mechanical except one line that depends on the Material 3
version in Compose BOM `2026.02.01`:

```kotlin
contentWindowInsets = { WindowInsets(0) }   // PantrySheet.kt
```

`ModalBottomSheet`'s inset parameter has changed shape across Material 3 releases. If it does
not resolve, drop that argument and keep only `imePadding()` on the content column: the
keyboard handling stays correct and the sheet merely re-applies its default navigation-bar
inset.

Run `./gradlew assembleDebug` first, then the device pass against §4.
