/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.sharedPreferences

import java.lang.reflect.Type

interface SharedPreferencesManager {

    fun saveBlocking(key: String, value: String): Boolean

    fun saveBlocking(key: String, value: Boolean): Boolean

    fun <T> saveBlocking(key: String, model: T): Boolean

    fun getString(key: String): String

    fun getString(key: String, defaultValue: String): String

    fun getInt(key: String, defaultValue: Int): Int

    fun getBoolean(key: String): Boolean

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    operator fun <T> get(key: String, type: Class<T>): T?

    operator fun <T> get(key: String, type: Type): T?

    fun clear()
}