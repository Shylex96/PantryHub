package com.pantryhub.core.model.product

import kotlinx.datetime.Instant

data class Product(
    val id: String,
    val name: String,
    val normalizedName: String,
    val categoryId: String? = null,
    val isFavorite: Boolean = false,
    val usageFrequency: Int = 0,
    val createdAt: Instant
)
