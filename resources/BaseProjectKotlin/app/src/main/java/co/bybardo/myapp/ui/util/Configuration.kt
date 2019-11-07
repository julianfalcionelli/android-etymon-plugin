/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.util

import co.bybardo.myapp.BuildConfig
import org.threeten.bp.Duration

object Configuration {
    // ETYMON-ALFA-GOOGLE-MAPS
    /**
     * Disable if you don't want to allow the user to mock its location with apps like FakeGps
     */
    const val IS_MOCK_LOCATION_ALLOWED = BuildConfig.MOCK_LOCATION_ALLOWED

    val LOCATION_UPDATES_INTERVAL = Duration.ofSeconds(10).toMillis()
    val LOCATION_UPDATES_FASTEST_INTERVAL = Duration.ofSeconds(5).toMillis()
    // ETYMON-OMEGA-GOOGLE-MAPS
}