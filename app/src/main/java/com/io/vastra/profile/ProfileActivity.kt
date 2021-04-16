package com.io.vastra.profile

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.io.vastra.R
import com.io.vastra.data.Award
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.system.exitProcess

class ProfileActivity: AppCompatActivity() {

    internal lateinit var rv: RecyclerView
    internal lateinit var adapter: AwardsAdapter
    internal lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        rv = findViewById(R.id.awards_rv)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = AwardsAdapter(exampleAwards())
        rv.adapter = adapter
        fab = findViewById(R.id.exit_button)
        fab.setOnClickListener {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            exitProcess(1);
        }
        val profile_button: ImageButton = findViewById(R.id.profile_button)
        profile_button.visibility = GONE

    }


    private fun exampleAwards(): Array<Award>{
        return arrayOf(Award(0, "Ran 1km"),
            Award(1, "Three workouts a week challenge completed"))
    }

}
