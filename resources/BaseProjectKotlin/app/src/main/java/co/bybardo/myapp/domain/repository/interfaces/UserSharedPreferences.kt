/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.domain.repository.interfaces

import io.reactivex.Completable
import io.reactivex.Single

interface UserSharedPreferences {
    fun clear(): Completable

    fun getCloudMessagingToken(): Single<String>

    fun saveCloudMessagingToken(token: String): Completable
}