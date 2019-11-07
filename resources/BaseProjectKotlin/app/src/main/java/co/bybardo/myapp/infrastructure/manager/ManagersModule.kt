/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager

import android.content.Context
import android.net.ConnectivityManager
import co.bybardo.myapp.infrastructure.manager.auth.AuthenticationManager
import co.bybardo.myapp.infrastructure.manager.auth.FirebaseAuthenticationManager
import co.bybardo.myapp.infrastructure.manager.calendar.CalendarManager
import co.bybardo.myapp.infrastructure.manager.calendar.CalendarManagerImpl
import co.bybardo.myapp.infrastructure.manager.file.FileManager
import co.bybardo.myapp.infrastructure.manager.file.FileManagerImpl
import co.bybardo.myapp.infrastructure.manager.internet.InternetManager
import co.bybardo.myapp.infrastructure.manager.serialization.ParserManager
import co.bybardo.myapp.infrastructure.manager.serialization.ParserManagerImpl
import co.bybardo.myapp.infrastructure.manager.serialization.gson.AnnotationExclusionStrategy
import co.bybardo.myapp.infrastructure.manager.sharedPreferences.SharedPreferencesManager
import co.bybardo.myapp.infrastructure.manager.sharedPreferences.SharedPreferencesManagerImpl
import co.bybardo.myapp.infrastructure.manager.systemSettings.SettingsManager
import co.bybardo.myapp.infrastructure.manager.systemSettings.SettingsManagerImpl
import co.bybardo.myapp.ui.util.date.DateUtils
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

// TODO ADD ALL MODULES
@Module
class ManagersModule {
    @Provides
    @Singleton
    fun providesSharedPreferencesManager(context: Context, parserManager: ParserManager):
        SharedPreferencesManager = SharedPreferencesManagerImpl(context, parserManager)

    @Provides
    fun providesParserManager(gson: Gson): ParserManager {
        return ParserManagerImpl(gson)
    }

    @Provides
    fun providesGson(): Gson {
        // TODO Check ISO FORMAT
        return GsonBuilder()
            .setDateFormat(DateUtils.ISO_8601_PATTERN)
            .setExclusionStrategies(AnnotationExclusionStrategy())
            .create()
    }

    @Provides
    fun providesInternetManager(context: Context): InternetManager {
        return InternetManager(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    @Provides
    @Singleton
    fun provideAuthenticationManager(): AuthenticationManager =
        FirebaseAuthenticationManager(FirebaseAuth.getInstance())

    @Provides
    fun providesSettingsManager(context: Context): SettingsManager =
        SettingsManagerImpl(LocationServices.getSettingsClient(context))

    @Provides
    fun providesFileManager(context: Context): FileManager = FileManagerImpl(context)

    @Provides
    fun providesCalendarManager(): CalendarManager = CalendarManagerImpl()
}