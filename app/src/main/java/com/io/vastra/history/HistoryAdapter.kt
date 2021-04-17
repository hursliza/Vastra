package com.io.vastra.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.io.vastra.R
import com.io.vastra.data.Award
import com.io.vastra.data.HistoryItem
import kotlin.time.ExperimentalTime

class HistoryAdapter(var dataSet: Array<HistoryItem>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val date: TextView
        val distance: TextView
        val duration: TextView

        init {
            date = view.findViewById(R.id.history_date)
            distance = view.findViewById(R.id.history_distance)
            duration = view.findViewById(R.id.history_duration)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)

        return ViewHolder(view)
    }

    @ExperimentalTime
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = dataSet[position].date.toString()
        holder.duration.text = dataSet[position].duration.toString()
        holder.distance.text = dataSet[position].distance
    }

    override fun getItemCount() = dataSet.size
}