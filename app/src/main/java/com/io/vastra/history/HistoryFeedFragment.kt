package com.io.vastra.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.vastra.R
import com.io.vastra.data.HistoryItem
import java.sql.Time
import java.util.*

class HistoryFeedFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_feed, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.history_feed)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val historyItems = arrayOf(
            HistoryItem(Time(1, 32, 45), "12.43 km", Date(2021, 4, 12)),
            HistoryItem(Time(0, 52, 28), "8.91 km", Date(2021, 4, 13))
        )
        recyclerView.adapter = HistoryAdapter(historyItems)
        return view
    }
}