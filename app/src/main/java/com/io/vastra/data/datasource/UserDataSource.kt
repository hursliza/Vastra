package com.io.vastra.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.io.vastra.data.entities.RunDescription
import com.io.vastra.data.entities.UserDetails
import kotlin.time.ExperimentalTime


class UserDataSource(val userId: String) {
    val currentUser: LiveData<UserDetails>
        get() = userLiveData;

    private val userLiveData = MutableLiveData<UserDetails>();

    init {
        configureDBConnection();
    }

    private fun configureDBConnection() {
        val userNode = Firebase.database.getReference(userId);
        userNode.addValueEventListener(object: ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedUserData = snapshot.getValue<UserDetails>();
                updatedUserData?.let {
                    userLiveData.value = it;
                }
            }
        })
    }


    fun addRunToHistory(runDescription: RunDescription) {
        val listRef = Firebase.database.getReference("${userId}/${UserDetails::runHistory.name}");
        val newNode = listRef.push();
        newNode.setValue(runDescription);
    }

    private fun checkIfRewardReceived(userDescription: UserDetails) {
        // TODO: implement
    };
}