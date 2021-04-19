package com.io.vastra.running.map_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.io.vastra.R

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style


class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private val LOCATION = LatLng(50.068123, 19.912484)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var mainView = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = mainView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)


        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS)
            val cameraPosition: CameraPosition = CameraPosition.Builder()
                .target(LOCATION) // set the camera's center position
                .zoom(15.0) // set the camera's zoom level
                .tilt(10.0) // set the camera's tilt
                .build()

            // Move the camera to that position
            mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        return mainView
    }
}