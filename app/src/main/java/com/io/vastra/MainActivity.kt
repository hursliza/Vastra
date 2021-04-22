package com.io.vastra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.io.vastra.data.datasource.UserDataSource
import com.io.vastra.data.datasource.UserDataSourceProvider
import com.io.vastra.history.HistoryFragment
import com.io.vastra.profile.ProfileActivity
import com.io.vastra.running.RunningFragment
import com.mapbox.mapboxsdk.Mapbox
import kotlin.time.ExperimentalTime

val DEFAULT_USER_ID = "42";

@ExperimentalTime
class MainActivity : AppCompatActivity() {
    internal lateinit var profileButton: ImageButton
    internal lateinit var tabs: TabLayout
    internal lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)
        profileButton = findViewById(R.id.profile_button)
        profileButton.setOnClickListener(profileButtonListener())

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        setCurrentUser();
        configureTabLayout();

    }


    private fun configureTabLayout() {
        val fragments = arrayListOf(HistoryFragment(), RunningFragment())

        viewPager = findViewById(R.id.viewpager)
        viewPager.adapter = ViewPagerAdapter(fragments, supportFragmentManager, lifecycle)

        tabs = findViewById(R.id.Tabs)
        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tabs.selectedTabPosition
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText(R.string.history)
                }
                1 -> {
                    tab.setText(R.string.running)
                }
            }

        }.attach()
    }

    private fun setCurrentUser() {
        UserDataSourceProvider.instantiate(DEFAULT_USER_ID);
    }


    private fun profileButtonListener(): View.OnClickListener? {
        return View.OnClickListener {
            intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}