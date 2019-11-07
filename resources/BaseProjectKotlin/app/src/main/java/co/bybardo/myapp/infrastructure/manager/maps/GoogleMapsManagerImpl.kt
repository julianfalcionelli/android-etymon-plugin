/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.maps

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class GoogleMapsManagerImpl : GoogleMapsManager {

    override fun addMarker(map: GoogleMap?, position: LatLng, iconRes: Int): Marker {
        val marker = MarkerOptions()
            .position(position)
            .icon(BitmapDescriptorFactory.fromResource(iconRes))
        return map?.addMarker(marker)!!
    }

    override fun changeCameraPosition(
        map: GoogleMap?,
        latLng: LatLng,
        cameraZoom: Float,
        bearing: Float,
        animate: Boolean
    ) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(cameraZoom)
            .bearing(bearing)
            .build()

        val cameraPositionUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        if (animate) {
            map?.animateCamera(cameraPositionUpdate)
        } else {
            map?.moveCamera(cameraPositionUpdate)
        }
    }

    override fun changeCameraPosition(
        map: GoogleMap?,
        location: LatLng,
        paddingFromEdges: Int,
        animate: Boolean
    ) {
        changeCameraPosition(map, arrayListOf(location), paddingFromEdges, animate)
    }

    override fun changeCameraPosition(
        map: GoogleMap?,
        startLocation: LatLng,
        destinationLocation: LatLng,
        paddingFromEdges: Int,
        animate: Boolean
    ) {
        changeCameraPosition(map, arrayListOf(startLocation, destinationLocation), paddingFromEdges, animate)
    }

    override fun changeCameraPosition(
        map: GoogleMap?,
        locations: ArrayList<LatLng>,
        paddingFromEdges: Int,
        animate: Boolean
    ) {
        val bounds = LatLngBounds.Builder()

        locations.forEach {
            bounds.include(it)
        }

        val cameraPositionUpdate = CameraUpdateFactory.newLatLngBounds(bounds.build(), paddingFromEdges)

        if (animate) {
            map?.animateCamera(cameraPositionUpdate)
        } else {
            map?.moveCamera(cameraPositionUpdate)
        }
    }

    override fun drawPolyline(
        map: GoogleMap?,
        decodedPointsList: List<LatLng>,
        polylineWidth: Float,
        color: Int
    ): Polyline? {
        return map?.addPolyline(PolylineOptions()
            .addAll(decodedPointsList)
            .width(polylineWidth)
            .color(color))
    }

    override fun mapDoublePointsToLatLng(polylinePoints: ArrayList<ArrayList<Double>>): ArrayList<LatLng> {
        val points = ArrayList<LatLng>()
        var i = 0
        while (i < polylinePoints.size) {
            points.add(LatLng(polylinePoints[i][0], polylinePoints[i][1]))
            i++
        }
        return points
    }

    override fun clearMap(map: GoogleMap?) {
        map?.clear()
    }
}