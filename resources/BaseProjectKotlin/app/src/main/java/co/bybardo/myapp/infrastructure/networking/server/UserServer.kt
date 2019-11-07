/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.networking.server

import co.bybardo.myapp.domain.model.User
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File

interface UserServer {
    fun createAccount(
        name: String,
        lastName: String
    ): Single<User>

    fun getProfile(): Single<User>

    fun updateProfile(
        user: User
    ): Single<User>

    fun removeAccount(): Completable

    fun updateAvatar(
        avatarFile: File
    ): Completable
}