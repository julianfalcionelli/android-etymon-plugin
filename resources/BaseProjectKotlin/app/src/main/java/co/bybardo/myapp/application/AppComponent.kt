/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.application

import co.bybardo.myapp.domain.repository.RepositoryModule
import co.bybardo.myapp.infrastructure.manager.ManagersModule
import co.bybardo.myapp.infrastructure.networking.NetModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main component of the app, created and persisted in the Application class.
 *
 * Whenever a new module is created, it should be added to the list of modules.
 * [AndroidSupportInjectionModule] is the module from Dagger.Android that helps with the
 * generation and location of subcomponents.
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    AppModule::class,
    NetModule::class,
    RepositoryModule::class,
    ManagersModule::class])
interface AppComponent : AndroidInjector<MyAppApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MyAppApplication>()
}
