/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.util

import java.util.Locale

object LanguageUtils {
    fun getCurrentLanguage(): Language {
        return when (Locale.getDefault().language) {
            "es" -> Language.SPANISH
            else -> Language.ENGLISH
        }
    }
}

enum class Language {
    SPANISH,
    ENGLISH,
}
