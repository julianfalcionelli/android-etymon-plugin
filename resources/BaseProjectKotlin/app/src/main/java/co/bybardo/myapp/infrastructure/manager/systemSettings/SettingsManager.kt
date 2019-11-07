/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.systemSettings

import com.google.android.gms.location.LocationRequest
import io.reactivex.Completable

interface SettingsManager {
    fun checkLocationSettings(locationRequest: LocationRequest): Completable
}