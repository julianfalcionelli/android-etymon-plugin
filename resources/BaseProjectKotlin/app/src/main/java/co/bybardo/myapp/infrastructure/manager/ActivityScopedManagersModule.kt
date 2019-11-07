/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager

import android.app.Activity
import android.content.Context
import co.bybardo.myapp.infrastructure.manager.maps.LocationManager
import co.bybardo.myapp.infrastructure.manager.maps.LocationManagerImpl
import co.bybardo.myapp.infrastructure.manager.permissions.PermissionsManager
import co.bybardo.myapp.infrastructure.manager.permissions.PermissionsManagerImpl
import co.bybardo.myapp.infrastructure.manager.systemSettings.SettingsManager
import dagger.Module
import dagger.Provides

@Module
class ActivityScopedManagersModule {

    @Provides
    fun providesPermissionsManager(activity: Activity):
        PermissionsManager = PermissionsManagerImpl(activity)

    @Provides
    fun providesLocationManager(
        context: Context,
        permissionsManager: PermissionsManager,
        settingsManager: SettingsManager
    ): LocationManager =
        LocationManagerImpl(context,
            permissionsManager,
            settingsManager)
}