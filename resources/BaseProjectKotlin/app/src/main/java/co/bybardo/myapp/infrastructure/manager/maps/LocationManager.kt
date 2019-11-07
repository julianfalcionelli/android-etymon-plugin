/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.maps

import android.Manifest
import android.content.Intent
import android.location.Address
import co.bybardo.myapp.BuildConfig
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Single
import org.threeten.bp.Duration

interface LocationManager {
    companion object {
        /**
         * Disable if you don't want to allow the user to mock its location with apps like FakeGps
         */
        const val IS_MOCK_LOCATION_ALLOWED = BuildConfig.MOCK_LOCATION_ALLOWED

        val LOCATION_UPDATES_INTERVAL = Duration.ofSeconds(10).toMillis()
        val LOCATION_UPDATES_FASTEST_INTERVAL = Duration.ofSeconds(5).toMillis()

        val LOCATION_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    fun canGetLocation(): Boolean
    fun askLocationAccessIntent(): Intent

    /**
     * Location Access Required
     */
    fun startLocationUpdates(): Observable<LatLng>
    fun getFirstLocationUpdate(): Single<LatLng>

    fun getLastLocation(): Single<LatLng>
    fun getLastLocationOrEmpty(): Single<LatLng>

    fun distanceBetween(startLatLng: LatLng, endLatLng: LatLng, unit: DistanceUnit): Double
    fun getLocationFromName(locationName: String): Single<LatLng>
    fun getAddressFromLatLng(latLng: LatLng): Address?

    fun stopLocationUpdates()

    fun stop()

    enum class DistanceUnit(val multiplier: Double) {
        METERS(1.0),
        MILES(0.000621371),
        FEET(3.28084)
    }
}