/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.domain.repository.implementation

import co.bybardo.myapp.domain.model.User
import co.bybardo.myapp.domain.repository.interfaces.SessionRepository
import co.bybardo.myapp.domain.repository.interfaces.UserSharedPreferences
import co.bybardo.myapp.infrastructure.manager.auth.AuthenticationManager
import co.bybardo.myapp.infrastructure.networking.RetrofitManager
import io.reactivex.Completable
import io.reactivex.Single

class SessionRepositoryImpl(
    private val authenticationManager: AuthenticationManager,
    private val retrofitManager: RetrofitManager,
    private val userSharedPreferences: UserSharedPreferences
) : SessionRepository {
    override fun createUser(email: String, password: String): Completable {
        return authenticationManager.createUser(email, password)
    }

    override fun updateUser(user: User): Completable {
        return authenticationManager.updateUser(user.name!!, user.lastName!!)
    }

    override fun updateEmail(newEmail: String): Completable {
        return authenticationManager.updateEmail(newEmail)
    }

    override fun updatePassword(oldPassword: String, newPassword: String): Completable {
        return authenticationManager.updatePassword(oldPassword, newPassword)
    }

    override fun checkIfUserExists(email: String): Single<Boolean> {
        return authenticationManager.checkIfUserExists(email)
    }

    override fun getUserEmail(): Single<String> {
        return authenticationManager.getUserEmail()
    }

    override fun getToken(): Single<String> {
        return authenticationManager.getToken()
    }

    override fun getUid(): Single<String> {
        return authenticationManager.getUid()
    }

    override fun isLogged(): Boolean {
        return authenticationManager.isLogged()
    }

    override fun sendVerificationEmail(): Completable {
        return authenticationManager.sendVerificationEmail()
    }

    override fun login(email: String, password: String): Completable {
        return authenticationManager.login(email, password)
    }

    override fun sendResetPasswordEmail(email: String): Completable {
        return authenticationManager.sendResetPasswordEmail(email)
    }

    override fun hasConfirmedEmail(): Single<Boolean> {
        return authenticationManager.hasConfirmedEmail()
    }

    override fun logout(): Completable {
        return authenticationManager.logout()
            .andThen {
                retrofitManager.clean()
                userSharedPreferences.clear()
            }
    }
}