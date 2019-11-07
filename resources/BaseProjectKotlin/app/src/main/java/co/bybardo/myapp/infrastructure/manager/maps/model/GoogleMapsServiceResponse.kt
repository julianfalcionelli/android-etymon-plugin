/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.maps.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

open class GoogleMapsServiceResponse(
    @SerializedName("geocoded_waypoints") val geocodedWaypoints: List<GeocodedWaypoint>,
    @SerializedName("routes") val routes: List<Route>,
    @SerializedName("status") val status: String
)

data class Route(
    @SerializedName("bounds") val bounds: Bounds,
    @SerializedName("copyrights") val copyrights: String,
    @SerializedName("legs") val legs: List<Leg>,
    @SerializedName("overview_polyline") val overviewPolyline: OverviewPolyline,
    @SerializedName("summary") val summary: String,
    @SerializedName("warnings") val warnings: List<Any>,
    @SerializedName("waypoint_order") val waypointOrder: List<Any>
)

data class OverviewPolyline(@SerializedName("points") val points: String)

data class Bounds(
    @SerializedName("northeast") val northeast: Northeast,
    @SerializedName("southwest") val southwest: Southwest
)

data class Northeast(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
) {
    val location: LatLng
        get() = LatLng(lat, lng)
}

data class Southwest(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
) {
    val location: LatLng
        get() = LatLng(lat, lng)
}

data class Leg(
    @SerializedName("distance") val distance: Distance,
    @SerializedName("duration") val duration: Duration,
    @SerializedName("end_address") val endAddress: String,
    @SerializedName("end_location") val endLocation: EndLocation,
    @SerializedName("start_address") val startAddress: String,
    @SerializedName("start_location") val startLocation: StartLocation,
    @SerializedName("steps") val steps: List<Step>,
    @SerializedName("traffic_speed_entry") val trafficSpeedEntry: List<Any>,
    @SerializedName("via_waypoint") val viaWaypoint: List<Any>
)

data class StartLocation(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
) {
    val location: LatLng
        get() = LatLng(lat, lng)
}

data class Duration(
    @SerializedName("text") val text: String,
    @SerializedName("value") val value: Int
)

data class Distance(
    @SerializedName("text") val text: String,
    @SerializedName("value") val value: Int
)

data class EndLocation(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
) {
    val location: LatLng
        get() = LatLng(lat, lng)
}

data class Step(
    @SerializedName("distance") val distance: Distance,
    @SerializedName("duration") val duration: Duration,
    @SerializedName("end_location") val endLocation: EndLocation,
    @SerializedName("html_instructions") val htmlInstructions: String,
    @SerializedName("polyline") val polyline: Polyline,
    @SerializedName("start_location") val startLocation: StartLocation,
    @SerializedName("travel_mode") val travelMode: String,
    @SerializedName("maneuver") val maneuver: String?
) {
    val maneuverType: ManeuverType
        get() {
            if (maneuver == null) {
                return ManeuverType.RIGHT
            }
            return when (maneuver) {
                "turn_right" -> ManeuverType.RIGHT
                "turn-slight-left" -> ManeuverType.TURN_SLIGHT_LEFT
                "turn-sharp-left" -> ManeuverType.TURN_SHARP_LEFT
                "uturn-left" -> ManeuverType.U_TURN_LEFT
                "turn-left" -> ManeuverType.TURN_LEFT
                "turn-slight-right" -> ManeuverType.TURN_SLIGHT_RIGHT
                "turn-sharp-right" -> ManeuverType.TURN_SHARP_RIGHT
                "uturn-right" -> ManeuverType.U_TURN_RIGHT
                "turn-right" -> ManeuverType.TURN_RIGHT
                "straight" -> ManeuverType.STRAIGHT
                "ramp-left" -> ManeuverType.RAMP_LEFT
                "ramp-right" -> ManeuverType.RAMP_RIGHT
                "merge" -> ManeuverType.MERGE
                "fork-left" -> ManeuverType.FORK_LEFT
                "fork-right" -> ManeuverType.FORK_RIGHT
                "ferry" -> ManeuverType.FERRY
                "ferry-train" -> ManeuverType.FERRY_TRAIN
                "roundabout-left" -> ManeuverType.ROUND_ABOUT_LEFT
                "roundabout-right" -> ManeuverType.ROUND_ABOUT_RIGHT
                else -> ManeuverType.RIGHT
            }
        }

    enum class ManeuverType {
        RIGHT, TURN_SLIGHT_LEFT, TURN_SHARP_LEFT, U_TURN_LEFT, TURN_LEFT, TURN_SLIGHT_RIGHT, TURN_SHARP_RIGHT,
        U_TURN_RIGHT, TURN_RIGHT, STRAIGHT, RAMP_LEFT, RAMP_RIGHT, MERGE, FORK_LEFT, FORK_RIGHT, FERRY, FERRY_TRAIN,
        ROUND_ABOUT_LEFT, ROUND_ABOUT_RIGHT
    }
}

data class Polyline(
    @SerializedName("points") val points: String
)

data class GeocodedWaypoint(
    @SerializedName("geocoder_status") val geocoderStatus: String,
    @SerializedName("place_id") val placeId: String,
    @SerializedName("types") val types: List<String>
)