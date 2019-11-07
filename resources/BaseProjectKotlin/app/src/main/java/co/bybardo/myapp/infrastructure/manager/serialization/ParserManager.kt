/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.serialization

import java.lang.reflect.Type

interface ParserManager {
    fun toJson(`object`: Any): String

    fun <T> fromJson(json: String, type: Class<T>): T

    fun <T> fromJson(json: String, type: Type): T
}
