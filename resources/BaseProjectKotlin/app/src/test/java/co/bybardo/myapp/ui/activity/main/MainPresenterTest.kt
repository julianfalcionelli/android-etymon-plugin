/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.activity.main

import co.bybardo.myapp.RxTrampolineSchedulerRule
import co.bybardo.myapp.infrastructure.manager.maps.LocationManager
import co.bybardo.myapp.infrastructure.manager.permissions.PermissionException
import co.bybardo.myapp.ui.activity.main.mvp.MainContract
import co.bybardo.myapp.ui.activity.main.mvp.MainPresenter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @get:Rule val testSchedulerRule = RxTrampolineSchedulerRule()

    @Mock lateinit var view: MainContract.View
    @Mock lateinit var locationManager: LocationManager
    @InjectMocks lateinit var presenter: MainPresenter

    @Test
    fun itShouldShowLocation() {
        given(locationManager.startLocationUpdates()).willReturn(Observable.just(LatLng(1.0, 1.0)))

        presenter.start()

        verify(view).showLocation(LatLng(1.0, 1.0))
    }

    @Test
    fun itShouldFinishIfLocationPermissionIsNotGranted() {
        given(locationManager.startLocationUpdates()).willReturn(Observable.error(
            PermissionException.PermissionDeniedException())
        )

        presenter.start()

        verify(view).finishScreen()
    }

    @Test
    fun itShouldFinishIfLocationIsNotEnabled() {
        val exception = Mockito.mock(ResolvableApiException::class.java)
        given(locationManager.startLocationUpdates()).willReturn(Observable.error(exception))

        presenter.start()
        presenter.onLocationNotEnabled()

        verify(view).showLocationSettingsDialog(exception)
        verify(view).finishScreen()
    }
}