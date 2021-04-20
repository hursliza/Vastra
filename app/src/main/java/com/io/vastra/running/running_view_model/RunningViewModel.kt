package com.io.vastra.running.running_view_model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.io.vastra.data.datasource.UserDataSourceProvider
import com.io.vastra.data.entities.RoutePoint
import com.io.vastra.data.entities.RunDescription
import java.util.*
import kotlin.concurrent.timer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

val METERS_IN_KILOMETR = 1000;


enum class RunViewModelState {
    Active,
    InActive
}

@ExperimentalTime
class RunningViewModel : ViewModel() {
    // region Observalbes
    private val _runDuration: MutableLiveData<Duration> = MutableLiveData(ZERO);
    val runDuration: LiveData<Duration>
        get() = _runDuration;


    private val _workoutStatistics: MutableLiveData<WorkoutStatistics> = MutableLiveData(
        WorkoutStatistics.empty
    );
    val workoutStatistics: LiveData<WorkoutStatistics>
        get() = _workoutStatistics;


    private var _state: MutableLiveData<RunViewModelState> = MutableLiveData(
        RunViewModelState.InActive
    );
    val state: LiveData<RunViewModelState>
        get() = _state;


    private val _currentLocation: MutableLiveData<RoutePoint> = MutableLiveData();
    val currentLocation: LiveData<RoutePoint>
        get() = _currentLocation;

    private val _runBreakpoints: MutableList<RunBreakpoint> = mutableListOf(RunBreakpoint.empty);
    val runBreakpoints: MutableList<RunBreakpoint>
        get() = _runBreakpoints
    //endregion


    //region Members
    private var _timer: Timer? = null;
    //endregion

    fun startRun() {
        _state.postValue(RunViewModelState.Active);
        startTimer();
    }

    private fun startTimer() {
        val timerPeriod = 100L;
        val milisecondsInSecond = 100;
        _timer = timer(
            name = "run timer", daemon = true,
            initialDelay = 0,
            period = timerPeriod
        ) {
            val previousDuration = _runDuration.value?.inSeconds ?: 0.toDouble();
            _runDuration.postValue((previousDuration + timerPeriod / milisecondsInSecond).seconds);
        }
    }

    fun addRoutePoint(newBreakpoint: RoutePoint) {
        val previousBreakpoint = _runBreakpoints.last()
        val distance = newBreakpoint.distanceTo(previousBreakpoint.point ?: newBreakpoint);
        val duration = (runDuration.value ?: ZERO).minus(previousBreakpoint.duration);
        _runBreakpoints.add(
            RunBreakpoint(
                distance = distance,
                duration = duration,
                point = newBreakpoint
            )
        );

        _currentLocation.postValue(newBreakpoint);
        recalculateStatistics(_runBreakpoints);
    }

    fun updateCurentLocation(actualBreakpoint: RoutePoint) {
        _currentLocation.postValue(actualBreakpoint);
    }

    private fun recalculateStatistics(runBreakpoints: List<RunBreakpoint>) {
        val avgPaces = calculatePacePerKm(runBreakpoints);
        val calories = calculateCalories(runBreakpoints, runDuration.value);
        val distance = calculateDistance(runBreakpoints);
        _workoutStatistics.postValue(
            WorkoutStatistics(
                avgPace = 0.0, //avgPaces.last(),
                calories = calories,
                distance = distance
            )
        );
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun endRun() {
        _state.postValue(RunViewModelState.InActive);
        _timer?.cancel();
        saveUserRun();
        cleanRunBreakouts();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveUserRun() {
        val runDescription = RunDescription().also {
            it.route = getRoute(_runBreakpoints);
            it.runDuration = runDuration.value?.toLong(DurationUnit.SECONDS);
            it.calories = calculateCalories(_runBreakpoints, runDuration.value)
            it.distance = calculateDistance(_runBreakpoints);
            it.pacePerKm = calculatePacePerKm(_runBreakpoints);
            it.runEndTimestamp = Date().toInstant().epochSecond
        }
        _workoutStatistics.postValue(WorkoutStatistics.empty)
        UserDataSourceProvider.instance.getDataSource().addRunToHistory(runDescription);
    }

    private fun getRoute(runBreakpoints: List<RunBreakpoint>): List<RoutePoint> =
        runBreakpoints.map { breakPoint -> breakPoint.point }.filterNotNull();

    private fun calculateCalories(
        runBreakpoints: List<RunBreakpoint>,
        runDuration: Duration?
    ): Int {
        val distance = calculateDistance(runBreakpoints)
        if (runDuration != null) {
            val velocity = distance / runDuration.inHours
            if (velocity < 8) {
                return (581 * runDuration.inHours).toInt()
            }
            if (velocity < 16) {
                return (1134 * runDuration.inHours).toInt()
            }
            return (1267 * runDuration.inHours).toInt()
        }
        return 0
    };

    private fun calculateDistance(runBreakpoints: List<RunBreakpoint>) =
        runBreakpoints.sumBy { it.distance }

    private fun calculatePacePerKm(runBreakpoints: List<RunBreakpoint>): List<Double> =
        runBreakpoints.groupByKm().map { 0.0 }
//        it.distance.toDouble() / it.duration.inSeconds.toInt() };

    private fun cleanRunBreakouts() {
        _runBreakpoints.clear()
        _runBreakpoints.add(RunBreakpoint.empty)
    }
}

class RunningViewModelFactory() : ViewModelProvider.Factory {
    @ExperimentalTime
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningViewModel::class.java)) {
            return RunningViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}