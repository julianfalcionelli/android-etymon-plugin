/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import co.bybardo.myapp.infrastructure.manager.serialization.ParserManager
import java.lang.reflect.Type

class SharedPreferencesManagerImpl(
    context: Context,
    private val parserManager: ParserManager,
    fileName: String = DEFAULT_FILE_NAME
) : SharedPreferencesManager {
    companion object {
        const val DEFAULT_FILE_NAME = "sharedPreferences"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    fun save(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun save(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun saveBlocking(key: String, value: String): Boolean {
        return sharedPreferences.edit().putString(key, value).commit()
    }

    override fun saveBlocking(key: String, value: Boolean): Boolean {
        return sharedPreferences.edit().putBoolean(key, value).commit()
    }

    override fun <T> saveBlocking(key: String, model: T): Boolean {
        return sharedPreferences.edit().putString(key, parserManager.toJson(model!!)).commit()
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun getString(key: String): String {
        return getString(key, "")
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: ""
    }

    fun getInt(key: String): Int {
        return getInt(key, -1)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun <T : Any> save(key: String, model: T) {
        sharedPreferences.edit().putString(key, parserManager.toJson(model)).apply()
    }

    override fun <T> get(key: String, type: Class<T>): T? {
        val json = getString(key)
        return if (json !== "") parserManager.fromJson(json, type) else null
    }

    override fun <T> get(key: String, type: Type): T? {
        val json = getString(key)
        return if (json !== "") parserManager.fromJson<T>(json, type) else null
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}