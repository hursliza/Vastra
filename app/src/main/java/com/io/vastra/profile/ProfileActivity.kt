package com.io.vastra.profile

import android.os.Bundle
import android.view.View.GONE
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.io.vastra.DEFAULT_USER_ID
import com.io.vastra.R
import kotlin.system.exitProcess

class ProfileActivity: AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel;


    internal lateinit var rv: RecyclerView
    internal lateinit var adapter: AwardsAdapter
    internal lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)
        setupSupportBar()

        rv = findViewById(R.id.awards_rv)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = AwardsAdapter()
        rv.adapter = adapter

        fab = findViewById(R.id.exit_button)
        fab.setOnClickListener {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            exitProcess(1);
        }

        val profile_button: ImageButton = findViewById(R.id.profile_button)
        profile_button.visibility = GONE


        configureViewModel();
    }

    private fun setupSupportBar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
    }

    fun configureViewModel() {
        val factory = ProfileViewModelFactory(this, DEFAULT_USER_ID);
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java);
        viewModel.userAwards.observe(this) {
            adapter.dataSet = it.toTypedArray();
            adapter.notifyDataSetChanged();
        }

    }



}
