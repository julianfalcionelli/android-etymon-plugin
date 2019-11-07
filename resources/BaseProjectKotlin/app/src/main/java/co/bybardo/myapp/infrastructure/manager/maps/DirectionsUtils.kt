/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.maps

import android.location.Location
import androidx.annotation.Keep
import com.google.android.gms.maps.model.LatLng

object DirectionsUtils {

    fun map(coordinate: LatLng): String = "${coordinate.latitude},${coordinate.longitude}"

    fun map(location: Location) = LatLng(location.latitude, location.longitude)
}

@Keep
enum class DirectionsType(val value: String) {
    DRIVING("driving"), WALKING("walking")
}
