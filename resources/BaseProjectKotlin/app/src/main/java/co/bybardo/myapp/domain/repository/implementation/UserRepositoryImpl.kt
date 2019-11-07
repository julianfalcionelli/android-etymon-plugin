/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.domain.repository.implementation

import android.content.Context
import co.bybardo.myapp.domain.model.User
import co.bybardo.myapp.domain.repository.interfaces.SessionRepository
import co.bybardo.myapp.domain.repository.interfaces.UserRepository
import co.bybardo.myapp.domain.repository.interfaces.UserSharedPreferences
import co.bybardo.myapp.infrastructure.notifications.FirebaseUtils
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

class UserRepositoryImpl(
    private val context: Context,
    private val sessionRepository: SessionRepository,
    private val userSharedPreferences: UserSharedPreferences
) : UserRepository {

    override fun initializeUser(): Completable {
        return Single.zip(
            sessionRepository.getUid(),
            sessionRepository.getUserEmail(),
            sessionRepository.hasConfirmedEmail(),
            Function3<String, String, Boolean, User> { id, email, hasConfirmedEmail ->
                User(id = id, email = email, emailVerified = hasConfirmedEmail)
            })
            .flatMapCompletable {
                // TODO Something ?
                Completable.complete()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun observeUser(): Observable<User> {
        return sessionRepository.getUid()
            .flatMapObservable {
                // TODO User Observable ?
                Observable.just(User())
            }
            .subscribeOn(Schedulers.io())
    }

    override fun saveCloudMessagingToken(token: String): Completable {
        return userSharedPreferences.saveCloudMessagingToken(token)
            .subscribeOn(Schedulers.io())
    }

    private fun getCloudMessagingToken(): Single<String> {
        return userSharedPreferences.getCloudMessagingToken()
            .flatMap { token ->
                if (token.isEmpty()) {
                    return@flatMap Single.fromCallable<String> {
                        FirebaseUtils.getPushNotificationsToken(context)
                    }
                } else {
                    return@flatMap Single.just(token)
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun checkDeviceRegistration(): Completable {
        return getCloudMessagingToken()
            .flatMapCompletable { token ->
                if (token.isNotEmpty()) {
                    registerDevice(token)
                } else {
                    Completable.complete()
                }
            }
            .subscribeOn(Schedulers.io())
    }

    fun registerDevice(token: String): Completable {
        // TODO Register Device In Backend
        return Completable.complete()
            .subscribeOn(Schedulers.io())
    }
}