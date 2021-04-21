package com.io.vastra.utils

import android.R.attr.end
import android.R.attr.start
import android.location.Location
import com.io.vastra.data.entities.RoutePoint
import com.io.vastra.running.running_view_model.METERS_IN_KILOMETR
import java.util.*

import kotlin.time.Duration
import kotlin.time.ExperimentalTime


inline fun <T:Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it !== null }) {
        closure(elements.filterNotNull());
    }
}

@ExperimentalTime
fun Duration.toVastraTimeString(): CharSequence?
        = String.format("%2d:%2d:%2d h", this.inHours.toInt(), this.inMinutes.toInt() % 60, this.inSeconds.toInt() % 60)


fun Int.toVastraDistanceString(): CharSequence? = String.format("%.2f km", this.toDouble() / 1000 );

fun Long?.toVastraDate(): Date = if(this === null) Date() else Date(this * 1000);

fun Long?.toVastraDateString(): CharSequence? = this.toVastraDate().toVastraDateString();
fun Date.toVastraDateString(): CharSequence? = java.text.SimpleDateFormat("yyyy-MM-dd").format(this)

@ExperimentalTime
fun Duration?.getDelta(other: Duration?) = (this ?: Duration.ZERO) - (other ?: Duration.ZERO);

@ExperimentalTime
fun Duration?.toMinutesPerKm(distance: Int) = METERS_IN_KILOMETR * (this?.inMinutes ?: 0.0) / distance;