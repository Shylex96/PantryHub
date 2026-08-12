package com.pantryhub.core.common.util

/**
 * Jaro-Winkler similarity in [0.0, 1.0] (1.0 = identical).
 *
 * Intended for comparing already-normalized names (see [toComparisonKey]). Used to flag
 * likely-but-not-exact duplicates on import (e.g. "lenteja" vs "lentejas").
 */
fun jaroWinklerSimilarity(a: String, b: String): Double {
    if (a == b) return 1.0
    if (a.isEmpty() || b.isEmpty()) return 0.0

    val jaro = jaroSimilarity(a, b)

    // Winkler boost: reward a common prefix of up to 4 characters.
    var prefix = 0
    val maxPrefix = minOf(4, minOf(a.length, b.length))
    while (prefix < maxPrefix && a[prefix] == b[prefix]) {
        prefix++
    }
    return jaro + prefix * 0.1 * (1.0 - jaro)
}

private fun jaroSimilarity(s1: String, s2: String): Double {
    val len1 = s1.length
    val len2 = s2.length
    val matchDistance = maxOf(0, (maxOf(len1, len2) / 2) - 1)

    val s1Matches = BooleanArray(len1)
    val s2Matches = BooleanArray(len2)
    var matches = 0

    for (i in 0 until len1) {
        val start = maxOf(0, i - matchDistance)
        val end = minOf(i + matchDistance + 1, len2)
        for (j in start until end) {
            if (s2Matches[j] || s1[i] != s2[j]) continue
            s1Matches[i] = true
            s2Matches[j] = true
            matches++
            break
        }
    }

    if (matches == 0) return 0.0

    var transpositions = 0.0
    var k = 0
    for (i in 0 until len1) {
        if (!s1Matches[i]) continue
        while (!s2Matches[k]) k++
        if (s1[i] != s2[k]) transpositions += 0.5
        k++
    }

    val m = matches.toDouble()
    return ((m / len1) + (m / len2) + ((m - transpositions) / m)) / 3.0
}
