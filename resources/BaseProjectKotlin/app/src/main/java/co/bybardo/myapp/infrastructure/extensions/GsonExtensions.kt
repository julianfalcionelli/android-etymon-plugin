/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.extensions

import androidx.annotation.RawRes
import co.bybardo.myapp.infrastructure.manager.file.FileManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.getElementsFromJson(@RawRes jsonFileID: Int, fileManager: FileManager): Single<T> =
    Single.fromCallable {
        this.fromJson<T>(fileManager.getStringFromFile(jsonFileID))
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())