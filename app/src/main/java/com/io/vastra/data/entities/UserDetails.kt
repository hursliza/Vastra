package com.io.vastra.data.entities

import java.io.Serializable

class UserDetails: Serializable {
    var userName: String? = null;
    var userAwards: List<AwardDetails> = listOf();
    var runHistory: List<RunDescription>? = listOf();
}
