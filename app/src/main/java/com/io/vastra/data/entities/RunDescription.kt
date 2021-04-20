package com.io.vastra.data.entities

import java.io.Serializable

class RunDescription: Serializable {
    var runEndTimestamp: Long? = 0;
    var route: List<RoutePoint> = listOf();
    var runDuration: Long? = null;
    var calories: Int? = null;
    var distance: Double? = null;
    var pacePerKm: List<Double> = listOf();
}
