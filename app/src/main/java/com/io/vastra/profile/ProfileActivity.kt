package com.io.vastra.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.io.vastra.R
import com.io.vastra.data.Award

class ProfileActivity: AppCompatActivity() {
    internal lateinit var rv: RecyclerView
    internal lateinit var adapter: AwardsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_profile)
            rv = findViewById(R.id.awards_rv)
            rv.layoutManager = LinearLayoutManager(this)
            adapter = AwardsAdapter(exampleAwards())
            rv.adapter = adapter
        }

    private fun exampleAwards(): Array<Award>{
        return arrayOf(Award(0, "Ran 1km"),
            Award(1, "Three workouts a week challenge completed"))

    }
}
