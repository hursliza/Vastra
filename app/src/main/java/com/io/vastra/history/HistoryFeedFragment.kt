package com.io.vastra.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.vastra.R
import com.io.vastra.data.datasource.UserDataSourceProvider
import com.io.vastra.data.models.HistoryItem
import java.sql.Time
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.seconds


@ExperimentalTime
class HistoryFeedFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_feed, container, false)
        configureRecyclerView(view);
        return view
    }

    private fun configureRecyclerView(historyFeedView: View) {
        val recyclerView: RecyclerView = historyFeedView.findViewById(R.id.history_feed)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = HistoryAdapter(arrayOf());
        recyclerView.adapter = adapter;
        attachDataSource(adapter);
    }

    private fun attachDataSource(adapter: HistoryAdapter) {
        UserDataSourceProvider.instance.getDataSource().currentUser.observe(viewLifecycleOwner) {
            val historyItems = it.runHistory?.values.orEmpty().map {
                runDescription ->
                HistoryItem(
                    duration = (runDescription.runDuration ?: 0).seconds,
                    distance = runDescription.distance.toString(),
                    date = if(runDescription.runEndTimestamp === null) Date() else Date(
                        runDescription.runEndTimestamp!! * 1000
                    )
                )
            }.toTypedArray();
            adapter.dataSet = historyItems;
            adapter.notifyDataSetChanged();
        }
    }
}