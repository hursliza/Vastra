package com.io.vastra.history.historyItem

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.io.vastra.R

class HistoryItemDetailsFragment : Fragment() {

    private val viewModel: HistoryItemViewModel by viewModels() {
        val idx = arguments?.getInt(HistoryDetailsStatisticsArgs.RunIdx.name)
            ?: throw InstantiationError("Cannot create details when run index is not provided");
        HistoryItemViewModelFactory(viewLifecycleOwner, idx);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.toHistoryFeed)
        }.isEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_item_details, container, false)
        var workoutDate = arguments?.getString("workoutDate")
        if (workoutDate == null) workoutDate = "Thu May 12 00:00:00 GMT"

        view.findViewById<TextView>(R.id.fragment_args).text = workoutDate

        return view
    }
}