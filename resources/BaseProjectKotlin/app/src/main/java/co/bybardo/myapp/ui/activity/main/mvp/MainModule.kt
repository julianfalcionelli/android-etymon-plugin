/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.activity.main.mvp

import android.app.Activity
import co.bybardo.myapp.infrastructure.manager.ActivityScopedManagersModule
import co.bybardo.myapp.infrastructure.manager.maps.LocationManager
import co.bybardo.myapp.ui.util.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [ActivityScopedManagersModule::class])
abstract class MainModule {

    @Binds
    @ActivityScoped
    abstract fun provideView(mainActivity: MainActivity): MainContract.View

    @Binds
    @ActivityScoped
    abstract fun provideActivity(activity: MainActivity): Activity

    /**
     * The method annotated with `@Provides` needs an instance, so we are making it static
     *
     * Reference: https://google.github.io/dagger/faq.html#why-cant-binds-and-instance-provides-methods-go-in-the-same-module
     */
    @Module
    companion object {
        @JvmStatic
        @Provides
        @ActivityScoped
        fun providePresenter(
            mainView: MainContract.View,
            locationManager: LocationManager
        ): MainContract.Presenter {
            return MainPresenter(mainView, locationManager)
        }
    }
}