package com.pantryhub.core.common.util

import java.text.Normalizer
import java.util.Locale

/**
 * Converts a string to a key suitable for comparison (lowercase, no accents, trimmed).
 * Example: "  MELÓN  " -> "melon"
 */
fun String.toComparisonKey(): String {
    return Normalizer.normalize(this.trim(), Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase(Locale.getDefault())
}

/**
 * Normalizes a string for storage/display (Capitalized first letter, rest lowercase, keeps accents, trimmed).
 * Example: "mElóN" -> "Melón"
 */
fun String.toStorageName(): String {
    val trimmed = this.trim()
    if (trimmed.isEmpty()) return ""
    
    return trimmed.lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
