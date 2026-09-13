package com.pantryhub.core.designsystem.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue

/**
 * The keyboard configuration every free-text field in PantryHub starts from.
 *
 * It is deliberately minimal — only [KeyboardCapitalization.Sentences]. Piling extra hints
 * on top (an explicit `keyboardType`, `autoCorrectEnabled`) is what made some IMEs drop the
 * auto-capitalization hint, which is the bug this default exists to avoid. Callers that need
 * an IME action copy this value instead of building a new [KeyboardOptions] from scratch:
 *
 * ```
 * keyboardOptions = PantryKeyboard.text.copy(imeAction = ImeAction.Done)
 * ```
 *
 * Fields that hold a *name* (list, product, category, note title) should additionally pass
 * `capitalizeFirstLetter = true`, which guarantees the capital regardless of the keyboard.
 */
object PantryKeyboard {
    /** Free text: sentence capitalization, nothing else. */
    val text: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
}

/** Uppercases the first character and leaves the rest of the text exactly as typed. */
fun String.capitalizeFirstChar(): String =
    if (isEmpty() || first().isUpperCase()) this else replaceFirstChar { it.uppercaseChar() }

@Composable
fun PantryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    capitalizeFirstLetter: Boolean = false,
    keyboardOptions: KeyboardOptions = PantryKeyboard.text,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = { text ->
            onValueChange(if (capitalizeFirstLetter) text.capitalizeFirstChar() else text)
        },
        // A floating label pushes the text up; use `placeholder` instead when the
        // field must stay a fixed, vertically-centered height (e.g. inline add bars).
        label = label?.let { text -> { Text(text) } },
        placeholder = placeholder?.let { text -> { Text(text) } },
        modifier = modifier,
        leadingIcon = leadingIcon,
        isError = isError,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

/**
 * [TextFieldValue] variant, for the few fields that need to control the selection (for
 * example placing the caret at the end when an edit sheet opens with existing text).
 *
 * Prefer the [String] overload everywhere else: hoisting a [TextFieldValue] that is rebuilt
 * on every recomposition resets the caret — and the IME's composing region with it — on
 * every keystroke.
 */
@Composable
fun PantryTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    capitalizeFirstLetter: Boolean = false,
    keyboardOptions: KeyboardOptions = PantryKeyboard.text,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = { new ->
            onValueChange(
                if (capitalizeFirstLetter) new.copy(text = new.text.capitalizeFirstChar()) else new
            )
        },
        label = label?.let { text -> { Text(text) } },
        placeholder = placeholder?.let { text -> { Text(text) } },
        modifier = modifier,
        leadingIcon = leadingIcon,
        isError = isError,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}
