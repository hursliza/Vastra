package com.io.vastra.running.details_fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.io.vastra.R

class DetailsFragment : Fragment() {
    private lateinit var runTime: TextView
    private lateinit var distance: TextView
    private lateinit var averagePace: TextView

    internal lateinit var fusedLocationClient: FusedLocationProviderClient
    internal lateinit var locationCallback: LocationCallback

    internal lateinit var startRunButton: FloatingActionButton

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
                    updateBreakpoints(location)
                    updateDetailsView()
                }
            }
        }
        startLocation(mainView)
        return mainView
    }

    private fun bindViews(mainView: View) {
        runTime = mainView.findViewById(R.id.time)
        distance = mainView.findViewById(R.id.distance)
        averagePace = mainView.findViewById(R.id.average_pace);

        startRunButton = mainView.findViewById(R.id.floatingActionButton)
        startRunButton.setOnClickListener { startRun() }
    }

    private fun startLocation(mainView: View) {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 5000
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

    private fun startRun() {
//        TODO("Change FAB functionality")
    }

    private fun updateBreakpoints(location: Location) {
//        TODO(
//            "Remove println and replace it by " +
//                    "addBreakpoint(RoutePoint(location.latitude, location.longitude))"
//        )
        println(location.toString())
    }

    private fun updateDetailsView() {
//        TODO("Update details info")
    }

}