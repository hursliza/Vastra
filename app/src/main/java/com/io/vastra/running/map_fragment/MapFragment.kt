package com.io.vastra.running.map_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe

import com.io.vastra.R
import com.io.vastra.running.running_view_model.RunningViewModel
import com.io.vastra.running.running_view_model.RunningViewModelFactory
import kotlin.time.ExperimentalTime

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style



 
@ExperimentalTime
class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private val LOCATION = LatLng(50.068123, 19.912484)
    
    private val viewModel: RunningViewModel by viewModels({requireParentFragment()}) {
        RunningViewModelFactory();
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = mainView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS)
            val actualPosition = getLastLocation(mainView)
            val cameraPosition: CameraPosition = CameraPosition.Builder()
                .target(actualPosition) // set the camera's center position
                .zoom(15.0) // set the camera's zoom level
                .tilt(10.0) // set the camera's tilt
                .build()
            mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        configureSubscriptions();
        return mainView;
    }


    fun configureSubscriptions() {
        viewModel.currentLocation.observe(viewLifecycleOwner) {
//            TODO: every 5 second new location is emitted here
//            Handle it and draw on map
        }
    }

    private fun getLastLocation(mainView: View): LatLng {
        return LOCATION
    }
}