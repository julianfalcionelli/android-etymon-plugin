/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.provider.Settings
import co.bybardo.myapp.infrastructure.manager.maps.LocationManager.Companion.IS_MOCK_LOCATION_ALLOWED
import co.bybardo.myapp.infrastructure.manager.maps.LocationManager.Companion.LOCATION_PERMISSIONS
import co.bybardo.myapp.infrastructure.manager.maps.LocationManager.Companion.LOCATION_UPDATES_FASTEST_INTERVAL
import co.bybardo.myapp.infrastructure.manager.maps.LocationManager.Companion.LOCATION_UPDATES_INTERVAL
import co.bybardo.myapp.infrastructure.manager.maps.exceptions.LocationException
import co.bybardo.myapp.infrastructure.manager.maps.exceptions.NetworkException
import co.bybardo.myapp.infrastructure.manager.permissions.PermissionsManager
import co.bybardo.myapp.infrastructure.manager.systemSettings.SettingsManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.Collections.emptyList
import java.util.Locale

class LocationManagerImpl(
    private val context: Context,
    private val permissionsManager: PermissionsManager,
    private val settingsManager: SettingsManager
) : LocationManager {
    companion object {
        val EMPTY_LOCATION = LatLng(0.0, 0.0)
    }

    private var locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationSettings: SettingsClient = LocationServices.getSettingsClient(context)
    private var locationCallbacks: ArrayList<LocationCallback> = arrayListOf()

    private val locationRequest = LocationRequest().apply {
        interval = LOCATION_UPDATES_INTERVAL
        fastestInterval = LOCATION_UPDATES_FASTEST_INTERVAL
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun canGetLocation(): Boolean {
        val androidLocationManager = getAndroidLocationManager()

        var isGpsEnabled = false
        var isNetworkEnabled = false

        // exceptions will be thrown if provider is not permitted.
        try {
            isGpsEnabled = androidLocationManager!!.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            isNetworkEnabled = androidLocationManager!!
                .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        return isGpsEnabled || isNetworkEnabled
    }

    private fun getAndroidLocationManager() =
        context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager?

    override fun askLocationAccessIntent() = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

    @SuppressLint("MissingPermission")
    override fun getLastLocation(): Single<LatLng> {
        return checkLocationSettings(locationRequest)
            .andThen(
                Single.create<LatLng> { emitter ->
                    locationClient.lastLocation
                        .addOnSuccessListener { lastLocation: Location? ->
                            if (lastLocation != null) {
                                emitter.onSuccess(LatLng(lastLocation.latitude, lastLocation.longitude))
                            } else {
                                emitter.onError(LocationException.EmptyLocationException())
                            }
                        }
                        .addOnFailureListener { exception ->
                            emitter.onError(exception)
                        }
                })
    }

    /**
     * Does not throw errors, it returns an empty location instead.
     */
    override fun getLastLocationOrEmpty(): Single<LatLng> {
        return getLastLocation().onErrorReturn { EMPTY_LOCATION }
    }

    override fun startLocationUpdates(): Observable<LatLng> {
        return permissionsManager.requestPermissions(LOCATION_PERMISSIONS)
            .andThen(settingsManager.checkLocationSettings(locationRequest))
            .andThen(requestLocationUpdates())
    }

    @SuppressLint("CheckResult")
    override fun getFirstLocationUpdate(): Single<LatLng> {
        return startLocationUpdates()
            .firstOrError()
            .flatMap { location ->
                Single.fromCallable {
                    stopLocationUpdates()
                    location
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(): Observable<LatLng> {
        return Observable.create<LatLng> { emitter ->
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val lastLocation = locationResult?.lastLocation
                    if (lastLocation == null) {
                        Timber.w("Null Location")
                        emitter.onError(Throwable("Last Location is null"))
                        return
                    }

                    Timber.d("Last location - lat:${lastLocation.latitude} lng: ${lastLocation.longitude}")
                    if (!IS_MOCK_LOCATION_ALLOWED && lastLocation.isFromMockProvider) {
                        Timber.w("Mock location not allowed")
                        emitter.onError(Throwable("Mock location not allowed"))
                        return
                    }

                    emitter.onNext(LatLng(lastLocation.latitude, lastLocation.longitude))
                }
            }

            val locationCallbackRef = LocationCallbackReference(locationCallback)
            locationCallbacks.add(locationCallbackRef)
            locationClient.requestLocationUpdates(locationRequest, locationCallbackRef, null)
            emitter.setCancellable { locationClient.removeLocationUpdates(locationCallbackRef) }
        }
    }

    override fun distanceBetween(startLatLng: LatLng, endLatLng: LatLng, unit: LocationManager.DistanceUnit): Double {
        val distanceInMeters = FloatArray(1)
        Location.distanceBetween(startLatLng.latitude,
            startLatLng.longitude,
            endLatLng.latitude,
            endLatLng.longitude,
            distanceInMeters)

        return distanceInMeters[0] * unit.multiplier
    }

    override fun getLocationFromName(locationName: String): Single<LatLng> {
        return Single.create<LatLng> { emitter ->
            val geocoder = Geocoder(context, Locale.getDefault())
            var locations: List<Address> = emptyList()
            try {
                locations = geocoder.getFromLocationName(locationName, 1)
            } catch (ioException: IOException) {
                emitter.onError(NetworkException())
            }

            if (locations.isNotEmpty() && locations[0].hasLatitude() && locations[0].hasLongitude()) {
                emitter.onSuccess(LatLng(locations[0].latitude, locations[0].longitude))
            } else {
                emitter.onError(Exception("Location not found"))
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun getAddressFromLatLng(latLng: LatLng): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).size > 0) {
            return geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
        } else {
            return null
        }
    }

    /**
     * Location API has a memory leak, use a WeakReference to avoid that.
     * <p>
     * val locationCallbackRef = LocationCallbackReference(locationCallback)
     * locationClient.requestLocationUpdates(locationRequest, locationCallbackRef, null)
     */
    private class LocationCallbackReference(locationCallback: LocationCallback) : LocationCallback() {
        private val locationCallbackRef: WeakReference<LocationCallback> = WeakReference(locationCallback)

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationCallbackRef.get()?.onLocationResult(locationResult)
        }
    }

    private fun checkLocationSettings(locationRequest: LocationRequest): Completable {
        return Completable.create { emitter ->
            val settingsBuilder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)

            locationSettings.checkLocationSettings(settingsBuilder.build())
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    Timber.w(it)
                    emitter.onError(it)
                }
        }
    }

    override fun stopLocationUpdates() {
        locationCallbacks.forEach { locationCallback -> locationClient.removeLocationUpdates(locationCallback) }
    }

    override fun stop() {
        stopLocationUpdates()
    }
}