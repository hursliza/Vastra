package com.io.vastra.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.io.vastra.R
import com.io.vastra.data.Award

class AwardsAdapter(var dataSet: Array<Award>): RecyclerView.Adapter<AwardsAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val description: TextView

        init {
            description = view.findViewById(R.id.award_description)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.award_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.description.text = dataSet[position].description
    }

    override fun getItemCount() = dataSet.size
}