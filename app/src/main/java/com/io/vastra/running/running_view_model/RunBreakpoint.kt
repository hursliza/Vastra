package com.io.vastra.running.running_view_model

import com.io.vastra.data.entities.RoutePoint
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
data class RunBreakpoint(var point: RoutePoint? = null, var duration: Duration? = null, var distance: Int) {
    companion object {
        val empty
        get() = RunBreakpoint(distance = 0);
    }
};

@ExperimentalTime
fun Collection<RunBreakpoint>.groupByKm() = this.fold(listOf(
    RunBreakpoint.empty
)) {
        acc, runBreakpoint ->
    val lastKmBreakpoint = acc.last();
    if (lastKmBreakpoint.distance < METERS_IN_KILOMETR) {
        lastKmBreakpoint.distance += runBreakpoint.distance;
        runBreakpoint.duration?.let {
            lastKmBreakpoint.duration = lastKmBreakpoint.duration ?: Duration.ZERO + it;
        }
        return acc;
    }
    return acc + RunBreakpoint.empty;
}