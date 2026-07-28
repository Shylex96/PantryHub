package com.pantryhub.core.database.mapper

import com.pantryhub.core.database.entity.CategoryEntity
import com.pantryhub.core.database.entity.ProductEntity
import com.pantryhub.core.database.entity.PurchaseEntity
import com.pantryhub.core.database.entity.PurchaseItemEntity
import com.pantryhub.core.database.entity.ShoppingItemEntity
import com.pantryhub.core.database.entity.ShoppingItemWithProduct
import com.pantryhub.core.database.entity.ShoppingListEntity
import com.pantryhub.core.model.category.Category
import com.pantryhub.core.model.product.Product
import com.pantryhub.core.model.purchase.Purchase
import com.pantryhub.core.model.shopping.ShoppingList
import com.pantryhub.core.model.shopping.ShoppingListItem

fun ProductEntity.asDomainModel() = Product(
    id = id,
    name = name,
    normalizedName = normalizedName,
    categoryId = categoryId,
    isFavorite = isFavorite,
    usageFrequency = usageFrequency,
    createdAt = createdAt
)

fun Product.asEntity() = ProductEntity(
    id = id,
    name = name,
    normalizedName = normalizedName,
    categoryId = categoryId,
    isFavorite = isFavorite,
    usageFrequency = usageFrequency,
    createdAt = createdAt
)

fun CategoryEntity.asDomainModel() = Category(
    id = id,
    name = name,
    icon = icon
)

fun Category.asEntity(normalizedName: String) = CategoryEntity(
    id = id,
    name = name,
    normalizedName = normalizedName,
    icon = icon
)

fun ShoppingListEntity.asDomainModel(items: List<ShoppingListItem>) = ShoppingList(
    id = id,
    name = name,
    type = type,
    items = items,
    createdAt = createdAt
)

fun ShoppingList.asEntity() = ShoppingListEntity(
    id = id,
    name = name,
    type = type,
    createdAt = createdAt
)

fun ShoppingItemEntity.asDomainModel(product: Product) = ShoppingListItem(
    id = id,
    shoppingListId = shoppingListId,
    product = product,
    quantity = quantity,
    isCompleted = isCompleted,
    price = price,
    addedAt = addedAt,
    completedAt = completedAt
)

fun ShoppingListItem.asEntity() = ShoppingItemEntity(
    id = id,
    shoppingListId = shoppingListId,
    productId = product.id,
    quantity = quantity,
    isCompleted = isCompleted,
    price = price,
    addedAt = addedAt,
    completedAt = completedAt
)

fun Purchase.asEntity() = PurchaseEntity(
    id = id,
    date = date,
    totalAmount = totalAmount,
    supermarket = supermarket,
    createdAt = date // Using date as creation time for simplicity initially
)

fun ShoppingListItem.asPurchaseItem(purchaseId: String) = PurchaseItemEntity(
    id = java.util.UUID.randomUUID().toString(),
    purchaseId = purchaseId,
    productId = product.id,
    quantity = quantity,
    price = price
)

fun ShoppingItemWithProduct.asDomainModel() = item.asDomainModel(product.asDomainModel())
