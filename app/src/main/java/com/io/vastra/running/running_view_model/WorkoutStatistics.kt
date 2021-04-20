package com.io.vastra.running.running_view_model

data class WorkoutStatistics(val avgPace: Double, val distance: Int, val calories: Int) {
    companion object {
        val empty
        get() = WorkoutStatistics(0.0,0,0)
    }
}