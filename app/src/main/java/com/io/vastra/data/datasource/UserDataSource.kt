package com.io.vastra.data.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.io.vastra.award.AwardConstraint
import com.io.vastra.award.RunMoreThanNTimesAward
import com.io.vastra.award.SUPPORTED_AWARDS
import com.io.vastra.data.entities.AwardDetails
import com.io.vastra.data.entities.RunDescription
import com.io.vastra.data.entities.UserDetails
import com.io.vastra.data.models.Award


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
        userLiveData.value?.let {
            val receivedAwards = getRecevideAwards(it);
            if (receivedAwards.count() != 0) {
                addAwardsToUser(receivedAwards);
            }
        }
    }

    private fun addAwardsToUser(receivedAwards: Collection<AwardDetails>) {
        val listRef = Firebase.database.getReference("${userId}/${UserDetails::userAwards.name}");
        receivedAwards.forEach {
            val newNode = listRef.push();
            newNode.setValue(it);
        }
    }

    private fun getRecevideAwards(userDescription: UserDetails): Collection<AwardDetails> {
        val awardsToReceive = SUPPORTED_AWARDS.filter { cnst -> userDescription.userAwards.values.find {
                it.id == cnst.id
            } == null
        }
        val completedConstraints = awardsToReceive.filter {  it.isCompleted(userDescription.runHistory?.values ?: listOf()) }
        return completedConstraints.map { AwardDetails().also{ aw ->
            aw.description = it.description
            aw.id = it.id
        }}
    };
}