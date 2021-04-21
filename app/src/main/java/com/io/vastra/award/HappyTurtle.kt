package com.io.vastra.award

import com.io.vastra.data.entities.RunDescription

class HappyTurtle: AwardConstraint {
    override val id: String
        get() = "happy_turtle";

    override val description: String
        get() = "Happy Turtle - in your history is incredibly low pace record. Probably you are a geek. Nothing personal";

    override fun isCompleted(userHistory: Collection<RunDescription>): Boolean {
        return userHistory.find {
            (it.pacePerKm.maxOrNull() ?: 0.0) < 2.0
        } != null;
    }
}