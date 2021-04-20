package com.io.vastra.running

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.io.vastra.data.datasource.UserDataSourceProvider
import com.io.vastra.data.entities.RoutePoint
import com.io.vastra.data.entities.RunDescription
import com.io.vastra.utils.euclidianDistance
import java.lang.Integer.max
import java.time.LocalDateTime.now
import java.util.*
import kotlin.concurrent.timer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

data class WorkoutStatistics(val avgPace: Double, val distance: Double, val calories: Int);


@ExperimentalTime
data class RunBreakpoint(val point: RoutePoint, val duration: Duration, val distance: Double);
@ExperimentalTime
val INITIAL_POINT = RunBreakpoint(RoutePoint(0.0, 0.0), 0.0.seconds, 0.0);


enum class RunViewModelState {
    Active,
    InActive
}

@ExperimentalTime
class RunningViewModel: ViewModel() {
    // region Observalbes
    private val _runDuration: MutableLiveData<Duration> = MutableLiveData(ZERO);
    val runDuration: LiveData<Duration>
        get() = _runDuration;


    private val _workoutStatistics: MutableLiveData<WorkoutStatistics> = MutableLiveData(
        WorkoutStatistics(0.0,0.0,0)
    );
    val workoutStatistics: LiveData<WorkoutStatistics>
    get() = _workoutStatistics;


    private var _state: MutableLiveData<RunViewModelState> = MutableLiveData(RunViewModelState.InActive);
    val state: LiveData<RunViewModelState>
    get() = _state;


    private val _currentLocation:  MutableLiveData<RoutePoint> = MutableLiveData();
    val currentLocation: LiveData<RoutePoint>
    get() = _currentLocation;
    //endregion


    //region Members
    private var _timer: Timer? = null;
    private val runBreakpoints: MutableList<RunBreakpoint> = mutableListOf(INITIAL_POINT);
    //endregion

    fun startRun() {
        _state.postValue(RunViewModelState.Active);
        startTimer();
    }

    private fun startTimer() {
        val timerPeriod = 100L;
        val milisecondsInSecond = 100;
        _timer = timer(name = "run timer", daemon = true,
            initialDelay = 0,
            period = timerPeriod
            ) {
            val previousDuration = _runDuration.value?.inSeconds ?: 0.toDouble();
            _runDuration.postValue(( previousDuration + timerPeriod / milisecondsInSecond).seconds);
        }
    }

    fun addRoutePoint(newBreakpoint: RoutePoint) {
        val previousBreakpoint = runBreakpoints.last();
        val distance = euclidianDistance(newBreakpoint, previousBreakpoint.point);
        val duration = (runDuration.value ?: ZERO).minus(previousBreakpoint.duration);
        runBreakpoints.add(
            RunBreakpoint(
                distance = distance,
                duration = duration,
                point = newBreakpoint
        ));

        _currentLocation.postValue(newBreakpoint);
        recalculateStatistics(runBreakpoints);
    }

    private fun recalculateStatistics(runBreakpoints: List<RunBreakpoint>) {
        val avgPaces = calculatePace(runBreakpoints);
        val calories = calculateCalories(runBreakpoints);
        val distance = calculateDistance(runBreakpoints);
        _workoutStatistics.postValue(WorkoutStatistics(
            avgPace = avgPaces.last(),
            calories = calories,
            distance = distance
)       );
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun endRun() {
        _state.postValue(RunViewModelState.InActive);
        _timer?.cancel();
        saveUserRun();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveUserRun() {
        val runDescription = RunDescription().also {
            it.route = getRoute(runBreakpoints);
            it.runDuration = runDuration.value?.toLong(DurationUnit.SECONDS);
            it.calories = calculateCalories(runBreakpoints);
            it.distance =  calculateDistance(runBreakpoints);
            it.pacePerKm = calculatePace(runBreakpoints);
            it.runEndTimestamp = Date().toInstant().epochSecond
        }

        UserDataSourceProvider.instance.getDataSource().addRunToHistory(runDescription);
    }


    private fun getRoute(runBreakpoints: List<RunBreakpoint>): List<RoutePoint>
            = runBreakpoints.map { breakPoint -> breakPoint.point };

    private fun calculateCalories(runBreakpoints: List<RunBreakpoint>): Int = 0;

    private fun calculateDistance(runBreakpoints: List<RunBreakpoint>)
            = runBreakpoints.sumByDouble { it.distance }

    private fun calculatePace(runBreakpoints: List<RunBreakpoint>): List<Double> = runBreakpoints.map { it.distance / max(it.duration.inMinutes.toInt(), 1) };

}

class RunningViewModelFactory(): ViewModelProvider.Factory {
    @ExperimentalTime
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningViewModel::class.java)) {
            return RunningViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}