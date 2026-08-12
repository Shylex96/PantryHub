package com.pantryhub.core.model.product

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class Product(
    val id: String,
    val name: String,
    val normalizedName: String,
    val categoryId: String? = null,
    val isFavorite: Boolean = false,
    val usageFrequency: Int = 0,
    val createdAt: Instant,
    // Alternative names for search (e.g. "papa" -> "Patata"). Display form.
    val aliases: List<String> = emptyList()
)
