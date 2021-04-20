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
        = Math.sqrt(Math.pow(x1.first - x2.first, 2.0) + Math.pow(x1.second - x2.second, 2.0))


@ExperimentalTime
fun Duration.toVastraTimeString(): CharSequence? {
    return String.format("%2d:%2d:%2d", this.inHours.toInt(), this.inMinutes.toInt() % 60, this.inSeconds.toInt() % 60)
}
