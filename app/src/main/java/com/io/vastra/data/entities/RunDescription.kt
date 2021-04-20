package com.io.vastra.data.entities

import java.io.Serializable

class RunDescription: Serializable {
    var runEndTimestamp: Long? = 0;
    var route: List<RoutePoint> = listOf();
    var runDuration: Long? = 0L;
    var calories: Int? = null;
    var distance: Double = 0.0;
    var pacePerKm: List<Double> = listOf();
}
