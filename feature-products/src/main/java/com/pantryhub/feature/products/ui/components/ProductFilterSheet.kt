package com.pantryhub.feature.products.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pantryhub.core.designsystem.R
import com.pantryhub.core.designsystem.ui.components.PantryButton
import com.pantryhub.core.designsystem.ui.components.PantryFieldLabel
import com.pantryhub.core.designsystem.ui.components.PantryOptionRow
import com.pantryhub.core.designsystem.ui.components.PantrySegmentedRow
import com.pantryhub.core.designsystem.ui.components.PantrySheet
import com.pantryhub.core.designsystem.ui.theme.PantryHubTheme
import com.pantryhub.core.designsystem.ui.theme.uncategorizedDotColor
import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.product.Product
import com.pantryhub.feature.products.presentation.ProductFilter
import com.pantryhub.feature.products.presentation.ProductSort

/**
 * Filter sheet for the Products tab (docs/04_UX_Guidelines.md "Category Browsing"):
 * "All categories" + every category with dot and count + "No category", a three-way
 * sort, a primary "Show N products" button and a "Reset" link. Changes are pending until
 * the button is pressed.
 */
@Composable
fun ProductFilterSheet(
    products: List<Product>,
    categories: List<Category>,
    currentFilter: ProductFilter,
    currentSort: ProductSort,
    onApply: (ProductFilter, ProductSort) -> Unit,
    onManageCategories: () -> Unit,
    onDismiss: () -> Unit
) {
    val spacing = PantryHubTheme.spacing
    var filter by remember { mutableStateOf(currentFilter) }
    var sort by remember { mutableStateOf(currentSort) }

    val knownIds = remember(categories) { categories.map { it.id }.toSet() }
    val countByCategory = remember(products) { products.groupingBy { it.categoryId }.eachCount() }
    val uncategorizedCount = remember(products, knownIds) {
        products.count { it.categoryId == null || it.categoryId !in knownIds }
    }
    val visibleCount = when (val f = filter) {
        ProductFilter.All -> products.size
        ProductFilter.Uncategorized -> uncategorizedCount
        is ProductFilter.ByCategory -> countByCategory[f.categoryId] ?: 0
    }

    PantrySheet(
        onDismissRequest = onDismiss,
        title = stringResource(R.string.filter_title),
        action = {
            TextButton(onClick = {
                filter = ProductFilter.All
                sort = ProductSort.CATEGORY
            }) {
                Text(
                    text = stringResource(R.string.reset_action),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) {
        PantryFieldLabel(stringResource(R.string.new_product_category_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            PantryOptionRow(
                label = stringResource(R.string.filter_all_categories),
                selected = filter == ProductFilter.All,
                onClick = { filter = ProductFilter.All },
                dotColor = MaterialTheme.colorScheme.primary,
                count = products.size
            )
            categories.forEach { category ->
                PantryOptionRow(
                    label = category.name,
                    selected = (filter as? ProductFilter.ByCategory)?.categoryId == category.id,
                    onClick = { filter = ProductFilter.ByCategory(category.id) },
                    dotColor = categoryColor(category, categories),
                    count = countByCategory[category.id] ?: 0
                )
            }
            PantryOptionRow(
                label = stringResource(R.string.product_no_category),
                selected = filter == ProductFilter.Uncategorized,
                onClick = { filter = ProductFilter.Uncategorized },
                dotColor = uncategorizedDotColor(),
                count = uncategorizedCount
            )
        }

        Spacer(modifier = Modifier.height(spacing.lg))
        PantryFieldLabel(stringResource(R.string.sort_by_label))
        Spacer(modifier = Modifier.height(spacing.sm))
        PantrySegmentedRow(
            options = listOf(
                stringResource(R.string.sort_category),
                stringResource(R.string.sort_name),
                stringResource(R.string.sort_most_used)
            ),
            selectedIndex = sort.ordinal,
            onSelect = { sort = ProductSort.entries[it] }
        )

        Spacer(modifier = Modifier.height(spacing.xl))
        PantryButton(
            onClick = { onApply(filter, sort) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(pluralStringResource(R.plurals.show_products_action, visibleCount, visibleCount))
        }
        Spacer(modifier = Modifier.height(spacing.xs))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = onManageCategories) {
                Text(stringResource(R.string.manage_categories_action))
            }
        }
    }
}
