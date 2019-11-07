/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.domain.repository.interfaces

import co.bybardo.myapp.domain.model.User
import io.reactivex.Completable
import io.reactivex.Single

interface SessionRepository {
    fun createUser(email: String, password: String): Completable
    fun updateUser(user: User): Completable
    fun updateEmail(newEmail: String): Completable
    fun updatePassword(oldPassword: String, newPassword: String): Completable
    fun checkIfUserExists(email: String): Single<Boolean>
    fun getUserEmail(): Single<String>
    fun getToken(): Single<String>
    fun getUid(): Single<String>
    fun isLogged(): Boolean
    fun sendVerificationEmail(): Completable
    fun login(email: String, password: String): Completable
    fun sendResetPasswordEmail(email: String): Completable
    fun hasConfirmedEmail(): Single<Boolean>
    fun logout(): Completable
}