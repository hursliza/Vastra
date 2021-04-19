package com.io.vastra.data.entities

import java.io.Serializable
import java.sql.Time
import java.util.*


class RunDescription: Serializable {
    var route: List<Pair<Double, Double>> = listOf();
    var runDuration: Long? = null;
    var calories: Int? = null;
    var distance: Float? = null;
    var pacePerKm: List<Float> = listOf();
}
