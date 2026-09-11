package com.pantryhub.feature.shopping.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.pantryhub.core.designsystem.ui.theme.categoryPalette
import com.pantryhub.core.designsystem.ui.theme.uncategorizedDotColor
import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.shopping.ShoppingListItem

/** Items of one category (or uncategorised when [category] is null) with its dot color. */
data class ItemGroup(
    val category: Category?,
    val color: Color,
    val items: List<ShoppingListItem>
)

/**
 * Groups list items by category in the categories' own order, appending an
 * "uncategorised" group (items with no category or a category that no longer exists).
 * Item order inside each group is preserved from the input.
 */
@Composable
fun groupItemsByCategory(
    items: List<ShoppingListItem>,
    categories: List<Category>
): List<ItemGroup> {
    val palette = categoryPalette()
    val grey = uncategorizedDotColor()
    val byCategory = items.groupBy { it.product.categoryId }
    val knownIds = categories.map { it.id }.toSet()

    val groups = categories.mapIndexedNotNull { index, category ->
        byCategory[category.id]?.let { grouped ->
            ItemGroup(category, palette[index % palette.size], grouped)
        }
    }
    val rest = byCategory
        .filterKeys { it == null || it !in knownIds }
        .values
        .flatten()

    return if (rest.isEmpty()) groups else groups + ItemGroup(null, grey, rest)
}

/** Color for a product's category dot, resolved the same way everywhere. */
@Composable
fun categoryDotColor(categoryId: String?, categories: List<Category>): Color {
    val palette = categoryPalette()
    val index = if (categoryId == null) -1 else categories.indexOfFirst { it.id == categoryId }
    return if (index >= 0) palette[index % palette.size] else uncategorizedDotColor()
}
