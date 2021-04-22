package com.io.vastra.award

import com.io.vastra.data.entities.RunDescription

class DoggedBeaver: AwardConstraint {
    override val id: String
        get() = "beaver"
    override val description: String
        get() = "Dogged beaver - you trained more than 7 times!"

    override fun isCompleted(userHistory: Collection<RunDescription>): Boolean = userHistory.count() > 7
}