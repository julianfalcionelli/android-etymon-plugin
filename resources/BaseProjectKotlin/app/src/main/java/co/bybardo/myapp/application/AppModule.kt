/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.application

import android.content.Context
import co.bybardo.myapp.ui.util.date.DateUtils
import dagger.Module
import dagger.Provides

/**
 * Defines all the classes that need to be provided in the scope of the app.
 *
 * Define here all objects that are shared throughout the app, like SharedPreferences, navigators or
 * others. If some of those objects are singletons, they should be annotated with `@Singleton`.
 */
@Module
class AppModule {
    @Provides
    fun providesContext(application: MyAppApplication): Context = application.applicationContext

    @Provides
    fun provideDateUtils(): DateUtils = DateUtils()
}