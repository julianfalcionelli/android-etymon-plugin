/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.domain.repository.interfaces

import co.bybardo.myapp.domain.model.User
import io.reactivex.Completable
import io.reactivex.Observable

interface UserRepository {
    fun initializeUser(): Completable
    fun observeUser(): Observable<User>
    fun saveCloudMessagingToken(token: String): Completable
    fun checkDeviceRegistration(): Completable
}