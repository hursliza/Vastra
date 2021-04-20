package com.io.vastra.award

import com.io.vastra.data.entities.RunDescription

class RunMoreThanNTimesAward(private val n: Int): AwardConstraint{
    override val id: String
        get() = "run_more_than_${n}_times";

    override val description: String
        get() = "Run more than ${n} times";

    override fun isCompleted(userHistory: Collection<RunDescription>): Boolean = userHistory.count() > n;

}