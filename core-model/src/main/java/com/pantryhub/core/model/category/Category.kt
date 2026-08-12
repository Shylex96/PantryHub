package com.pantryhub.core.model.category

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val icon: String? = null
)
