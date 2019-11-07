/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.networking.server

import co.bybardo.myapp.domain.model.User
import co.bybardo.myapp.infrastructure.networking.request.SignUpRequest
import co.bybardo.myapp.infrastructure.networking.request.UpdateProfileRequest
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserServerImpl(private var userServerAPI: UserServerAPI) : UserServer {

    override fun createAccount(
        name: String,
        lastName: String
    ): Single<User> {
        return userServerAPI.createAccount(SignUpRequest(name, lastName))
            .subscribeOn(Schedulers.io())
    }

    override fun getProfile(): Single<User> {
        return userServerAPI.getProfile()
            .subscribeOn(Schedulers.io())
    }

    override fun updateProfile(user: User): Single<User> {
        return userServerAPI.updateProfile(
            UpdateProfileRequest(
                user.name!!,
                user.lastName!!
            )
        )
            .subscribeOn(Schedulers.io())
    }

    override fun removeAccount(): Completable {
        return userServerAPI.removeAccount()
            .subscribeOn(Schedulers.io())
    }

    override fun updateAvatar(avatarFile: File): Completable {
        val filePart = MultipartBody.Part.createFormData("file", avatarFile.name,
            RequestBody.create(MediaType.parse("image/png"), avatarFile))

        return userServerAPI.updateAvatar(filePart)
            .subscribeOn(Schedulers.io())
    }
}