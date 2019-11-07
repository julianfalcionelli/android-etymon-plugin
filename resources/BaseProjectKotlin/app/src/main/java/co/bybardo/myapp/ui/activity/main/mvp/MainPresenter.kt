/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.activity.main.mvp

import co.bybardo.myapp.infrastructure.manager.maps.LocationManager
import co.bybardo.myapp.infrastructure.manager.permissions.PermissionException
import com.google.android.gms.common.api.ResolvableApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class MainPresenter(
    private val view: MainContract.View,
    private val locationManager: LocationManager
) : MainContract.Presenter {
    private val subscriptions = CompositeDisposable()

    override fun init() {
        view.initToolbar(true, view.getToolbarTitle())
    }

    override fun start() {
        locationManager.startLocationUpdates()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.showLocation(it)
            }, { exception ->
                when (exception) {
                    is PermissionException.PermissionDeniedException,
                    is PermissionException.PermissionPermanentlyDeniedException ->
                        // Location permission denied
                        view.finishScreen()
                    is ResolvableApiException ->
                        // Enable location
                        view.showLocationSettingsDialog(exception)
                    else ->
                        Timber.w(exception)
                }
            }).addTo(subscriptions)
    }

    override fun onToolbarBackPressed() {
        view.finishScreen()
    }

    override fun onLocationEnabled() {
        start()
    }

    override fun onLocationNotEnabled() {
        view.finishScreen()
    }

    override fun destroy() {
        subscriptions.dispose()
    }
}
