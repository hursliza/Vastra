package com.io.vastra.running.details_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.observe
import com.io.vastra.R
import com.io.vastra.data.datasource.UserDataSourceProvider
import com.io.vastra.data.entities.RunDescription

class DetailsFragment : Fragment() {
    private lateinit var runTime: TextView;
    private lateinit var distance: TextView;
    private lateinit var averagePace: TextView;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainView = inflater.inflate(R.layout.fragment_details, container, false);
        bindViews(mainView);
        return mainView;
    }

    private fun bindViews(mainView: View) {
       runTime = mainView.findViewById(R.id.time)
       distance = mainView.findViewById(R.id.distance)
       averagePace = mainView.findViewById(R.id.average_pace);
    }


}