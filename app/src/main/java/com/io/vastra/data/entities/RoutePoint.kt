package com.io.vastra.data.entities

import android.location.Location

class RoutePoint(var lat: Double = 0.0, var long: Double = 0.0) {
    /**
     * Returns distance in meters
     * */
    fun distanceTo(other: RoutePoint): Int {
        val location1 = Location("locationA").also {
            it.latitude = lat;
            it.longitude = long
        };
        val location2 = Location("locationB").also {
            it.latitude = other.long;
            it.longitude = other.long
        }
        return location1.distanceTo(location2).toInt();
    }
}
