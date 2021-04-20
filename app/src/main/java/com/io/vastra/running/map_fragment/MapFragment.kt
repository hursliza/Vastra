package com.io.vastra.running.map_fragment

import android.graphics.Color
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
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import kotlin.time.ExperimentalTime

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style


@ExperimentalTime
class MapFragment : Fragment() {

    private lateinit var mapView: MapView

    private val viewModel: RunningViewModel by viewModels({ requireParentFragment() }) {
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
        configureSubscriptions();
        return mainView;
    }


    private fun configureSubscriptions() {
        viewModel.currentLocation.observe(viewLifecycleOwner) {
            mapView.getMapAsync { mapboxMap ->
                mapboxMap.setStyle(Style.MAPBOX_STREETS)
                val actualPosition = LatLng(it.lat, it.long)
                val cameraPosition: CameraPosition = CameraPosition.Builder()
                    .target(actualPosition) // set the camera's center position
                    .zoom(15.0) // set the camera's zoom level
                    .build()
                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                mapboxMap.clear()
                mapboxMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(actualPosition))
                        .title("Your actual location")
                )

                if (viewModel.runBreakpoints.isNotEmpty()) {
                    val points = viewModel.runBreakpoints
                        .filter { runBreakpoint -> runBreakpoint.point != null }
                        .map { runBreakpoint ->
                            LatLng(
                                runBreakpoint.point!!.lat,
                                runBreakpoint.point!!.long
                            )
                        }
                    mapboxMap.addPolyline(
                        PolylineOptions()
                            .addAll(points)
                            .color(Color.parseColor("#3bb2d0"))
                            .width(2f)
                    );
                }
            }
        }
    }

}