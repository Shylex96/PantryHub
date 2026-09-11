package com.pantryhub.feature.settings.ui

import java.util.Locale

/**
 * Languages the app ships, as BCP-47 tags. The empty tag means "follow the system", which
 * is the default: a fresh install never forces a language.
 *
 * To add a language: add `values-<tag>/strings.xml` in core-designsystem, add the tag here,
 * in `app/src/main/res/xml/locales_config.xml` and in `androidResources.localeFilters`
 * (app/build.gradle.kts). Labels are derived from the locale itself, so no new string is
 * needed for the picker.
 */
object AppLanguages {
    const val SYSTEM = ""

    val supported: List<String> = listOf(SYSTEM, "en", "es")

    /**
     * Display name of a language in its own language ("Español", "English"), capitalised.
     * The system entry has no locale and is labelled by the caller.
     */
    fun nativeName(tag: String): String {
        val locale = Locale.forLanguageTag(tag)
        return locale.getDisplayLanguage(locale).replaceFirstChar { c ->
            if (c.isLowerCase()) c.titlecase(locale) else c.toString()
        }
    }
}
