/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.domain.repository.implementation

import co.bybardo.myapp.domain.repository.interfaces.UserSharedPreferences
import co.bybardo.myapp.infrastructure.manager.sharedPreferences.SharedPreferencesManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UserSharedPreferencesImpl(
    private val sharedPreferencesManager: SharedPreferencesManager
) : UserSharedPreferences {
    companion object {
        private const val USER_SP_MESSAGING_TOKEN = "USER_SP_MESSAGING_TOKEN"
    }

    override fun clear(): Completable {
        return Completable.fromAction {
            sharedPreferencesManager.clear()
        }.subscribeOn(Schedulers.io())
    }

    override fun getCloudMessagingToken(): Single<String> {
        return Single.fromCallable {
            return@fromCallable sharedPreferencesManager.getString(USER_SP_MESSAGING_TOKEN, "")
        }.subscribeOn(Schedulers.io())
    }

    override fun saveCloudMessagingToken(token: String): Completable {
        return Completable.fromAction {
            sharedPreferencesManager.saveBlocking(USER_SP_MESSAGING_TOKEN, token)
        }.subscribeOn(Schedulers.io())
    }
}
