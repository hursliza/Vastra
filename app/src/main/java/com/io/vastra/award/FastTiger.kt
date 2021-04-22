package com.io.vastra.award

import com.io.vastra.data.entities.RunDescription

class FastTiger: AwardConstraint {
    override val id: String
        get() = "fast_tiger";

    override val description: String
        get() = "Fast Tiger - Wow, you achieved really high pace!"

    override fun isCompleted(userHistory: Collection<RunDescription>): Boolean = userHistory.any { (it.pacePerKm.minOrNull() ?: 0.0) > 5.0 };
}