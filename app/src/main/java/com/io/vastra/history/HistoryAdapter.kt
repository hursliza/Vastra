package com.io.vastra.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.io.vastra.R
import com.io.vastra.data.models.HistoryItem
import com.io.vastra.history.historyItem.HistoryDetailsStatisticsArgs
import com.io.vastra.utils.toVastraDateString
import com.io.vastra.utils.toVastraDistanceString
import com.io.vastra.utils.toVastraTimeString
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime


@ExperimentalTime
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = dataSet[position].date.toVastraDateString()
        holder.duration.text = dataSet[position].duration.toVastraTimeString();
        holder.distance.text = dataSet[position].distance.toVastraDistanceString()

        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val bundle = bundleOf(HistoryDetailsStatisticsArgs.RunIdx.name to position)
                val navController = Navigation.findNavController(holder.itemView)
                navController.navigate(R.id.toDetails, bundle)
            }
        })
    }

    override fun getItemCount() = dataSet.size
}


