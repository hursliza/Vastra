package com.io.vastra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.io.vastra.profile.ProfileActivity

class MainActivity : AppCompatActivity() {
    internal lateinit var profileButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        profileButton = findViewById(R.id.profile_button)
        profileButton.setOnClickListener(profileButtonListener())
    }

    private fun profileButtonListener(): View.OnClickListener? {
        return View.OnClickListener {
            intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}