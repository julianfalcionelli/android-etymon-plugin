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
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserServerAPI {
    @POST("me")
    fun createAccount(
        @Body request: SignUpRequest
    ): Single<User>

    @GET("me")
    fun getProfile(): Single<User>

    @PUT("me")
    fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Single<User>

    @DELETE("me")
    fun removeAccount(): Completable

    @Multipart
    @POST("me/avatar")
    fun updateAvatar(
        @Part avatarFile: MultipartBody.Part
    ): Completable
}