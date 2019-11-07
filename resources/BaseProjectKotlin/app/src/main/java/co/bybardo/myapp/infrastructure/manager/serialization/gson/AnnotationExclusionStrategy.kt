/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.serialization.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class AnnotationExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipField(f: FieldAttributes): Boolean = f.getAnnotation(Exclude::class.java) != null

    override fun shouldSkipClass(clazz: Class<*>): Boolean = false
}