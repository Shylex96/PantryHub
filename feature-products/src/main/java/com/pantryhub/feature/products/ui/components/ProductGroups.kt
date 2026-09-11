package com.pantryhub.feature.products.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.pantryhub.core.designsystem.ui.theme.categoryPalette
import com.pantryhub.core.designsystem.ui.theme.uncategorizedDotColor
import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.product.Product
import com.pantryhub.feature.products.presentation.ProductFilter
import com.pantryhub.feature.products.presentation.ProductSort

/** Header of a group in the Products tab. */
sealed interface ProductGroupHeader {
    /** Favorite products, pulled out of their categories (gold star label). */
    data object Favorites : ProductGroupHeader
    data class ByCategory(val category: Category, val color: Color) : ProductGroupHeader
    /** Products without a (known) category — grey dot, "No category". */
    data object Uncategorized : ProductGroupHeader
    /** Flat lists (A–Z, Most used) have no header. */
    data object None : ProductGroupHeader
}

data class ProductGroup(
    val header: ProductGroupHeader,
    val products: List<Product>
)

/**
 * Applies [filter] and [sort] to [products] and returns the groups to render
 * (docs/04_UX_Guidelines.md "Category Browsing", docs/05 §6.7). Products are expected
 * to be already sorted by name.
 */
@Composable
fun rememberProductGroups(
    products: List<Product>,
    categories: List<Category>,
    filter: ProductFilter,
    sort: ProductSort
): List<ProductGroup> {
    val palette = categoryPalette()
    return remember(products, categories, filter, sort, palette) {
        val knownIds = categories.map { it.id }.toSet()
        val filtered = when (filter) {
            ProductFilter.All -> products
            ProductFilter.Uncategorized -> products.filter { it.categoryId == null || it.categoryId !in knownIds }
            is ProductFilter.ByCategory -> products.filter { it.categoryId == filter.categoryId }
        }

        when (sort) {
            ProductSort.NAME -> listOf(ProductGroup(ProductGroupHeader.None, filtered))
            ProductSort.MOST_USED -> listOf(
                ProductGroup(
                    ProductGroupHeader.None,
                    filtered.sortedWith(
                        compareByDescending<Product> { it.usageFrequency }.thenBy { it.name.lowercase() }
                    )
                )
            )
            ProductSort.CATEGORY -> {
                val favorites = filtered.filter { it.isFavorite }
                val rest = filtered.filter { !it.isFavorite }
                val byCategory = rest.groupBy { it.categoryId }
                val groups = mutableListOf<ProductGroup>()
                if (favorites.isNotEmpty()) groups += ProductGroup(ProductGroupHeader.Favorites, favorites)
                categories.forEachIndexed { index, category ->
                    byCategory[category.id]?.let { grouped ->
                        groups += ProductGroup(
                            ProductGroupHeader.ByCategory(category, palette[index % palette.size]),
                            grouped
                        )
                    }
                }
                val uncategorized = byCategory
                    .filterKeys { it == null || it !in knownIds }
                    .values
                    .flatten()
                if (uncategorized.isNotEmpty()) {
                    groups += ProductGroup(ProductGroupHeader.Uncategorized, uncategorized)
                }
                groups
            }
        }
    }
}

/** Color for a product's category dot, resolved the same way everywhere. */
@Composable
fun productDotColor(categoryId: String?, categories: List<Category>): Color {
    val palette = categoryPalette()
    val index = if (categoryId == null) -1 else categories.indexOfFirst { it.id == categoryId }
    return if (index >= 0) palette[index % palette.size] else uncategorizedDotColor()
}

/** Color for a category by its position in the list. */
@Composable
fun categoryColor(category: Category, categories: List<Category>): Color =
    productDotColor(category.id, categories)
