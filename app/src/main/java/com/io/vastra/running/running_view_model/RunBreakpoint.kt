package com.io.vastra.running.running_view_model

import com.io.vastra.data.entities.RoutePoint
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
data class RunBreakpoint(var point: RoutePoint, var duration: Duration, var distance: Int) {
    companion object {
        val empty
        get() = RunBreakpoint(RoutePoint(0.0, 0.0),
            0.0.seconds,
            0);
    }
};

@ExperimentalTime
fun List<RunBreakpoint>.groupByKm() = this.fold(listOf(
    RunBreakpoint.empty
)) {
        acc, runBreakpoint ->
    val lastKmBreakpoint = acc.last();
    if (lastKmBreakpoint.distance < METERS_IN_KILOMETR) {
        lastKmBreakpoint.distance += runBreakpoint.distance;
        lastKmBreakpoint.duration += runBreakpoint.duration;
        return acc;
    }
    return acc + RunBreakpoint.empty;
}