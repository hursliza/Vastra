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
import com.io.vastra.data.entities.RunDescription
import com.io.vastra.utils.toVastraDateString

class HistoryItemDetailsFragment : Fragment() {

    private lateinit var workoutDate: TextView;
    val viewModel: HistoryItemViewModel by viewModels() {
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
        bindViews(view);
        configureDataSource()
        return view
    }

    fun bindViews(mainView: View) {
        workoutDate = mainView.findViewById(R.id.fragment_args)
    }


    private fun configureDataSource() {
        viewModel.runDescription.observe(viewLifecycleOwner) {
            workoutDate.text = it.runEndTimestamp.toVastraDateString()
            updateView(it)
        }
    }

    private fun updateView(description: RunDescription){
        description.route
    }
}