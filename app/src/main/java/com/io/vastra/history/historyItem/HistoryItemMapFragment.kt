package com.io.vastra.history.historyItem

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe

import com.io.vastra.R
import com.io.vastra.data.entities.RunDescription
import com.mapbox.mapboxsdk.annotations.PolylineOptions

import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style


class HistoryItemMapFragment : Fragment() {

    private lateinit var mapView: MapView

    val viewModel: HistoryItemViewModel by viewModels() {
        val idx = parentFragment?.arguments?.getInt(HistoryDetailsStatisticsArgs.RunIdx.name)
            ?: throw InstantiationError("Cannot create details when run index is not provided")
        HistoryItemViewModelFactory(viewLifecycleOwner, idx)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView = inflater.inflate(R.layout.fragment_history_item_map, container, false)
        mapView = mainView.findViewById(R.id.historyItemMapView)
        mapView.onCreate(savedInstanceState)
        configureDataSource();
        return mainView;
    }

    private fun configureDataSource() {
        viewModel.runDescription.observe(viewLifecycleOwner) {
            updateView(it)
        }
    }

    private fun updateView(description: RunDescription) {
        mapView.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS)
            val centralRouteIndex = description.route.size / 2
            val centralRoutePoint = description.route[centralRouteIndex]
            val centralPosition = LatLng(centralRoutePoint.lat, centralRoutePoint.long)

            val cameraPosition: CameraPosition = CameraPosition.Builder()
                .target(centralPosition) // set the camera's center position
                .zoom(15.0) // set the camera's zoom level
                .build()
            mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            val points = description.route
                .map { runBreakpoint ->
                    LatLng(
                        runBreakpoint.lat,
                        runBreakpoint.long
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