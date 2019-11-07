/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.internet

import android.net.ConnectivityManager

class InternetManager(private val connectivityManager: ConnectivityManager) {

    val isOnline: Boolean
        get() {
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

    fun onWifi(): Boolean {
        return !connectivityManager.isActiveNetworkMetered
    }
}
