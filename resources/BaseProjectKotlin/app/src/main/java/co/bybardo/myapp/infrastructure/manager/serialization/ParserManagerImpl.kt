/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.serialization

import com.google.gson.Gson
import java.lang.reflect.Type

class ParserManagerImpl(private val gson: Gson) : ParserManager {

    override fun toJson(`object`: Any): String {
        return gson.toJson(`object`)
    }

    override fun <T> fromJson(json: String, type: Class<T>): T {
        return gson.fromJson(json, type)
    }

    override fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson<T>(json, type)
    }
}