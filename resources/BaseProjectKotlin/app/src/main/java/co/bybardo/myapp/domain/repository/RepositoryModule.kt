/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.domain.repository

import android.content.Context
import co.bybardo.myapp.domain.repository.implementation.SessionRepositoryImpl
import co.bybardo.myapp.domain.repository.implementation.UserRepositoryImpl
import co.bybardo.myapp.domain.repository.implementation.UserSharedPreferencesImpl
import co.bybardo.myapp.domain.repository.interfaces.SessionRepository
import co.bybardo.myapp.domain.repository.interfaces.UserRepository
import co.bybardo.myapp.domain.repository.interfaces.UserSharedPreferences
import co.bybardo.myapp.infrastructure.manager.auth.AuthenticationManager
import co.bybardo.myapp.infrastructure.manager.serialization.ParserManager
import co.bybardo.myapp.infrastructure.manager.sharedPreferences.SharedPreferencesManagerImpl
import co.bybardo.myapp.infrastructure.networking.RetrofitManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserSharedPreferences(context: Context, parserManager: ParserManager):
        UserSharedPreferences = UserSharedPreferencesImpl(
        SharedPreferencesManagerImpl(
            context, parserManager, "USER_SP"
        )
    )

    @Provides
    @Singleton
    fun provideSessionRepository(
        authenticationManager: AuthenticationManager,
        retrofitManager: RetrofitManager,
        userSharedPreferences: UserSharedPreferences
    ): SessionRepository {
        return SessionRepositoryImpl(authenticationManager,
            retrofitManager,
            userSharedPreferences)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        context: Context,
        sessionRepository: SessionRepository,
        userSharedPreferences: UserSharedPreferences
    ): UserRepository {
        return UserRepositoryImpl(
            context,
            sessionRepository,
            userSharedPreferences
        )
    }
}