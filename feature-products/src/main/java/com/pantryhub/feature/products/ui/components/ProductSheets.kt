package com.pantryhub.feature.products.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryFieldLabel
import com.pantryhub.core.designsystem.ui.components.PantryOptionRow
import com.pantryhub.core.designsystem.ui.components.PantryKeyboard
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.components.PantryTextField
import com.pantryhub.core.designsystem.ui.components.rememberSheetFocusRequester
import com.pantryhub.core.designsystem.ui.components.shake
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.designsystem.ui.theme.uncategorizedDotColor
import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.product.Product

/** "New product" sheet: name + category. */
@Composable
fun NewProductSheet(
    categories: List<Category>,
    onCreate: (name: String, categoryId: String?) -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    val haptic = LocalHapticFeedback.current
    var name by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf<String?>(null) }
    val focusRequester = rememberSheetFocusRequester()
    var shakeTrigger by remember { mutableStateOf(0) }


    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.fab_new_product),
        subtitle = stringResource(R.string.sheet_new_product_subtitle)
    ) {
        PantryFieldLabel(stringResource(R.string.field_name_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(R.string.product_name_placeholder),
            singleLine = true,
            capitalizeFirstLetter = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .shake(shakeTrigger),
            keyboardOptions = PantryKeyboard.text.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (name.isNotBlank()) {
                        onCreate(name.trim(), categoryId)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    } else {
                        shakeTrigger++
                    }
                }
            )
        )
        Spacer(modifier = Modifier.height(spacing.lg))
        CategoryPicker(
            categories = categories,
            selectedId = categoryId,
            onSelect = { categoryId = it }
        )
        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = {
                if (name.isNotBlank()) {
                    onCreate(name.trim(), categoryId)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                } else {
                    shakeTrigger++
                }
            },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.create_product_action))
        }
    }
}

/** Edit an existing product: its name, its category and its aliases (comma-separated). */
@Composable
fun EditProductSheet(
    product: Product,
    categories: List<Category>,
    onSave: (name: String, categoryId: String?, aliases: List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    val haptic = LocalHapticFeedback.current
    var nameValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = product.name,
                selection = TextRange(product.name.length)
            )
        )
    }
    var categoryId by remember { mutableStateOf(product.categoryId) }
    var aliasesInput by remember { mutableStateOf(product.aliases.joinToString(", ")) }
    val focusRequester = rememberSheetFocusRequester()
    var shakeTrigger by remember { mutableStateOf(0) }


    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.edit_product_sheet_title),
        subtitle = stringResource(R.string.sheet_edit_product_subtitle)
    ) {
        PantryFieldLabel(stringResource(R.string.field_name_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = nameValue,
            onValueChange = { nameValue = it },
            placeholder = stringResource(R.string.product_name_placeholder),
            singleLine = true,
            capitalizeFirstLetter = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .shake(shakeTrigger),
            keyboardOptions = PantryKeyboard.text.copy(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(spacing.lg))
        CategoryPicker(
            categories = categories,
            selectedId = categoryId,
            onSelect = { categoryId = it }
        )
        Spacer(modifier = Modifier.height(spacing.lg))
        PantryFieldLabel(stringResource(R.string.field_aliases_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantryTextField(
            value = aliasesInput,
            onValueChange = { aliasesInput = it },
            placeholder = stringResource(R.string.field_aliases_placeholder),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = PantryKeyboard.text.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (nameValue.text.isNotBlank()) {
                        val aliases = aliasesInput
                            .split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                        onSave(nameValue.text.trim(), categoryId, aliases)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    } else {
                        shakeTrigger++
                    }
                }
            )
        )
        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = {
                if (nameValue.text.isNotBlank()) {
                    val aliases = aliasesInput
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                    onSave(nameValue.text.trim(), categoryId, aliases)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                } else {
                    shakeTrigger++
                }
            },
            enabled = nameValue.text.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

/**
 * Move several products to one category at once (multi-select in Products,
 * docs/04_UX_Guidelines.md "Category Browsing").
 */
@Composable
fun AssignCategorySheet(
    productCount: Int,
    categories: List<Category>,
    onAssign: (categoryId: String?) -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var categoryId by remember { mutableStateOf<String?>(null) }
    var touched by remember { mutableStateOf(false) }

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.assign_category_title),
        subtitle = pluralStringResource(R.plurals.assign_category_subtitle, productCount, productCount)
    ) {
        CategoryPicker(
            categories = categories,
            selectedId = categoryId,
            onSelect = {
                categoryId = it
                touched = true
            }
        )
        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = { onAssign(categoryId) },
            enabled = touched,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(pluralStringResource(R.plurals.assign_category_action, productCount, productCount))
        }
    }
}

/** "No category" + every category as selectable option rows. */
@Composable
internal fun CategoryPicker(
    categories: List<Category>,
    selectedId: String?,
    onSelect: (String?) -> Unit
) {
    val spacing = PantryHubTheme.spacing
    PantryFieldLabel(stringResource(R.string.new_product_category_label))
    Spacer(modifier = Modifier.height(spacing.sm))
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        PantryOptionRow(
            label = stringResource(R.string.product_no_category),
            selected = selectedId == null,
            onClick = { onSelect(null) },
            dotColor = uncategorizedDotColor()
        )
        categories.forEach { category ->
            PantryOptionRow(
                label = category.name,
                selected = selectedId == category.id,
                onClick = { onSelect(category.id) },
                dotColor = categoryColor(category, categories)
            )
        }
    }
}
