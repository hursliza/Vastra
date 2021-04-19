package com.io.vastra.running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.io.vastra.R
import kotlin.time.ExperimentalTime

@ExperimentalTime
class RunningFragment: Fragment() {

    private val viewModel: RunningViewModel by viewModels {
        RunningViewModelFactory();
    };

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_running, container, false)
        return view
    }

}