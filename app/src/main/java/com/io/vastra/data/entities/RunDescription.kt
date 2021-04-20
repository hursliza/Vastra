package com.io.vastra.data.entities

import com.io.vastra.running.running_view_model.RunBreakpoint
import java.io.Serializable

class RunDescription: Serializable {
    var runEndTimestamp: Long? = 0;
    var route: List<RoutePoint> = listOf();
    var runDuration: Long? = 0L;
    var calories: Int? = null;
    var distance: Int = 0;
    var pacePerKm: List<Double> = listOf();
}
