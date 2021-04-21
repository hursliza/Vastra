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
import com.io.vastra.running.running_view_model.RunViewModelState
import com.io.vastra.running.running_view_model.RunningViewModel
import com.io.vastra.running.running_view_model.RunningViewModelFactory
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import kotlin.time.ExperimentalTime


@ExperimentalTime
class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private val MAKI_ICON_AIRPORT = "airport-15"

    private var state: RunViewModelState = RunViewModelState.InActive;
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
        viewModel.state.observe(viewLifecycleOwner) {
            state = it
        }

        viewModel.currentLocation.observe(viewLifecycleOwner) {
            mapView.getMapAsync { mapboxMap ->
                val actualRoutePoint = viewModel.currentLocation.value!!
                val actualPosition = LatLng(actualRoutePoint.lat, actualRoutePoint.long)

                mapboxMap.setStyle(Style.MAPBOX_STREETS)
                updateCameraPosition(mapboxMap, actualPosition)
                updateMapMarker(mapboxMap, actualPosition)

                when (state) {
                    RunViewModelState.Active -> {
                        updateMapPolyline(mapboxMap)
                    }
                }
            }
        }
    }

    private fun updateCameraPosition(mapboxMap: MapboxMap, actualPosition: LatLng) {
        val cameraPosition: CameraPosition = CameraPosition.Builder()
            .target(actualPosition) // set the camera's center position
            .zoom(15.0) // set the camera's zoom level
            .build()
        mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    private fun updateMapMarker(mapboxMap: MapboxMap, actualPosition: LatLng) {
        val iconFactory = IconFactory.getInstance(mapView.context)
        val icon = iconFactory.fromResource(R.drawable.mapbox_compass_icon)

        mapboxMap.clear()
        mapboxMap.addMarker(
            MarkerOptions()
                .position(LatLng(actualPosition))
                .icon(icon)
        )
    }

    private fun updateMapPolyline(mapboxMap: MapboxMap) {
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