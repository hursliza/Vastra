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
fun List<RunBreakpoint>.groupByKm(): Collection<RunBreakpoint> {
    val accumulated = mutableListOf(this[0]);
    this.drop(1).forEach { item ->
        val acc = RunBreakpoint(distance = 0).also {
            val previousBreakpoint =  accumulated.last();
            it.distance = item.distance + previousBreakpoint.distance
            if (previousBreakpoint.duration == null) {
                previousBreakpoint.duration = Duration.ZERO;
            }
            it.duration = (item.duration ?: Duration.ZERO) + previousBreakpoint.duration!!;

        }
        accumulated.add(acc);
    }
        val groupedByKilometer = accumulated.groupBy { it.distance / METERS_IN_KILOMETR }
    return groupedByKilometer.values.map { it.last() };
}