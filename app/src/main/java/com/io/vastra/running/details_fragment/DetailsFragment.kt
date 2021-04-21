package com.io.vastra.running.details_fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.gms.location.LocationRequest
import com.io.vastra.R
import com.io.vastra.data.entities.RoutePoint
import com.io.vastra.running.running_view_model.RunViewModelState
import com.io.vastra.running.running_view_model.RunningViewModel
import com.io.vastra.running.running_view_model.RunningViewModelFactory
import com.io.vastra.running.running_view_model.WorkoutStatistics
import com.io.vastra.utils.toVastraDistanceString
import com.io.vastra.utils.toVastraTimeString
import kotlin.time.ExperimentalTime

enum class Command {
    StartRun,
    ResumeRun,
    EndRun,
    PauseRun
}

@ExperimentalTime
class DetailsFragment : Fragment() {

    private lateinit var runTime: TextView;
    private lateinit var distance: TextView;
    private lateinit var averagePace: TextView;
    private lateinit var calories: TextView;
    private lateinit var fab_start: FloatingActionButton
    private lateinit var fab_stop: FloatingActionButton
    private lateinit var fab_pause: FloatingActionButton
    private var state: RunViewModelState = RunViewModelState.InActive;

    internal lateinit var fusedLocationClient: FusedLocationProviderClient;
    internal lateinit var locationCallback: LocationCallback;

    private val viewModel: RunningViewModel by viewModels({ requireParentFragment() }) {
        RunningViewModelFactory();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView = inflater.inflate(R.layout.fragment_details, container, false);
        bindViews(mainView);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainView.context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    updateBreakpoints(location);
                }
            }
        }

        configureSubscriptions();
        startLocation(mainView);
        return mainView;
    }


    private fun configureSubscriptions() {
        viewModel.runDuration.observe(viewLifecycleOwner) {
            runTime.text = it.toVastraTimeString();
        }
        viewModel.state.observe(viewLifecycleOwner) {
            state = it;
        }
        viewModel.workoutStatistics.observe(viewLifecycleOwner) {
            updateView(it);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindViews(mainView: View) {
        runTime = mainView.findViewById(R.id.time);
        distance = mainView.findViewById(R.id.distance);
        averagePace = mainView.findViewById(R.id.average_pace);

        fab_start = mainView.findViewById(R.id.fab_start)
        fab_stop = mainView.findViewById(R.id.fab_stop)
        fab_pause = mainView.findViewById(R.id.fab_pause)
        fab_start.setOnClickListener(detailsFABOnClickListener(Command.StartRun))
        fab_pause.setOnClickListener(detailsFABOnClickListener(Command.PauseRun))
        fab_stop.setOnClickListener(detailsFABOnClickListener(Command.EndRun))

        calories = mainView.findViewById(R.id.calories);
    }


    private fun startLocation(mainView: View) {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                mainView.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mainView.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private fun updateBreakpoints(location: Location) {
        val routePoint = RoutePoint(
            location.latitude,
            location.longitude
        )
        when (state) {
            RunViewModelState.InActive -> viewModel.updateCurentLocation(routePoint)
            RunViewModelState.Active -> viewModel.addRoutePoint(routePoint)
        }
    }


    private fun updateView(workoutStatistics: WorkoutStatistics) {
        distance.text = workoutStatistics.distance.toVastraDistanceString();
        averagePace.text =
            getString(R.string.average_pace_history_details, workoutStatistics.avgPace);
        calories.text = getString(R.string.calories, workoutStatistics.calories);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleRunState() {
        when (state) {
            RunViewModelState.InActive -> viewModel.startRun();
            RunViewModelState.Active -> viewModel.endRun();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun detailsFABOnClickListener(command: Command?): View.OnClickListener? {
        return View.OnClickListener {
            when (state) {
                RunViewModelState.Active -> onRunActive()
                RunViewModelState.Paused -> command?.let { it1 -> onRunPaused(it1) }
                RunViewModelState.InActive -> onRunInActive()
            }
        }
    }

    fun onRunActive() {
        viewModel.pauseRun()
        fab_stop.visibility = View.VISIBLE
        fab_start.visibility = View.VISIBLE
        fab_pause.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onRunPaused(command: Command){
        if (command == Command.ResumeRun){
            viewModel.resumeRun()
            fab_pause.visibility = View.VISIBLE
            fab_stop.visibility = View.GONE
            fab_start.visibility = View.GONE
        }
        if (command == Command.EndRun){
            viewModel.endRun()
            fab_stop.visibility = View.GONE
            fab_start.visibility = View.VISIBLE
            fab_start.setOnClickListener(detailsFABOnClickListener(Command.StartRun))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onRunInActive(){
        viewModel.startRun()
        fab_start.visibility = View.GONE
        fab_start.setOnClickListener(detailsFABOnClickListener(Command.ResumeRun))
        fab_pause.visibility = View.VISIBLE
    }
}
