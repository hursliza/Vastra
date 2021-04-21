package com.io.vastra.award

import com.io.vastra.data.entities.RunDescription

interface AwardConstraint {
    val id: String;
    val description: String;
    fun isCompleted(userHistory: Collection<RunDescription>): Boolean;
}