/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.maps

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline

interface GoogleMapsManager {
    companion object {
        const val POLYLINE_DEFAULT_WIDTH = 10f
        const val DEFAULT_CAMERA_ZOOM = 17.1f
        const val DEFAULT_BEARING = 0f
    }

    fun addMarker(map: GoogleMap?, position: LatLng, iconRes: Int): Marker

    fun changeCameraPosition(map: GoogleMap?, latLng: LatLng, cameraZoom: Float, bearing: Float, animate: Boolean)
    fun changeCameraPosition(
        map: GoogleMap?,
        location: LatLng,
        paddingFromEdges: Int,
        animate: Boolean
    )
    fun changeCameraPosition(
        map: GoogleMap?,
        startLocation: LatLng,
        destinationLocation: LatLng,
        paddingFromEdges: Int,
        animate: Boolean
    )
    fun changeCameraPosition(
        map: GoogleMap?,
        locations: ArrayList<LatLng>,
        paddingFromEdges: Int,
        animate: Boolean
    )

    fun drawPolyline(map: GoogleMap?, decodedPointsList: List<LatLng>, polylineWidth: Float, color: Int): Polyline?
    fun mapDoublePointsToLatLng(polylinePoints: ArrayList<ArrayList<Double>>): ArrayList<LatLng>

    fun clearMap(map: GoogleMap?)
}