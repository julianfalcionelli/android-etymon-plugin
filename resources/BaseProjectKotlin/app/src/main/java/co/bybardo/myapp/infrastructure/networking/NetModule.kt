/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.networking

import android.app.Application
import co.bybardo.myapp.domain.repository.interfaces.SessionRepository
import co.bybardo.myapp.infrastructure.manager.internet.InternetManager
import co.bybardo.myapp.infrastructure.networking.server.UserServer
import co.bybardo.myapp.infrastructure.networking.server.UserServerAPI
import co.bybardo.myapp.infrastructure.networking.server.UserServerImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun provideRetrofitManager(
        application: Application,
        gson: Gson,
        sessionRepository: SessionRepository,
        internetManager: InternetManager
    ): RetrofitManager = RetrofitManager(application, gson, sessionRepository, internetManager)

    @Provides
    fun provideUserServerAPI(retrofitManager: RetrofitManager): UserServerAPI {
        return retrofitManager.customRetrofit?.create(UserServerAPI::class.java)!!
    }

    @Provides
    fun provideUserServer(userServerAPI: UserServerAPI): UserServer {
        return UserServerImpl(userServerAPI)
    }
}
