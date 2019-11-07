/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.extensions

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

// TODO Check if this works

@SuppressLint("CheckResult")
fun Completable.toUI(): Completable {
    observeOn(AndroidSchedulers.mainThread())
    return this
}

@SuppressLint("CheckResult")
fun <T> Observable<T>.toUI(): Observable<T> {
    observeOn(AndroidSchedulers.mainThread())
    return this
}

@SuppressLint("CheckResult")
fun <T> Maybe<T>.toUI(): Maybe<T> {
    observeOn(AndroidSchedulers.mainThread())
    return this
}

@SuppressLint("CheckResult")
fun <T> Single<T>.toUI(): Single<T> {
    observeOn(AndroidSchedulers.mainThread())
    return this
}