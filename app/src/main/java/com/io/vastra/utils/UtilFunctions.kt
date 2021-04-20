package com.io.vastra.utils

import com.io.vastra.data.entities.RoutePoint
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

inline fun <T:Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it !== null }) {
        closure(elements.filterNotNull());
    }
}

fun euclidianDistance(x1: RoutePoint, x2: RoutePoint)
        = Math.sqrt(Math.pow(x1.lat - x2.lat, 2.0) + Math.pow(x1.long - x2.long, 2.0))


@ExperimentalTime
fun Duration.toVastraTimeString(): CharSequence?
        = String.format("%2d:%2d:%2d h", this.inHours.toInt(), this.inMinutes.toInt() % 60, this.inSeconds.toInt() % 60)


fun Double.toVastraDistanceString(): CharSequence? = String.format("%.2f km", this);
