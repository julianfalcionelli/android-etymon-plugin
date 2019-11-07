/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.activity.main.mvp

import co.bybardo.myapp.ui.activity.base.BaseContract
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.LatLng

interface MainContract {
    interface View : BaseContract.View {
        fun initToolbar(backEnabled: Boolean, toolbarTitle: String)
        fun getToolbarTitle(): String
        fun finishScreen()
        fun showLocationSettingsDialog(exception: ResolvableApiException)
        fun showLocation(location: LatLng)
    }

    interface Presenter : BaseContract.Presenter {
        fun init()
        fun onToolbarBackPressed()
        fun start()
        fun onLocationEnabled()
        fun onLocationNotEnabled()
    }
}
