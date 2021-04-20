package com.io.vastra.data.entities

import java.io.Serializable

class UserDetails: Serializable {
    var userName: String? = null;
    var userAwards: HashMap<String, AwardDetails> = hashMapOf();
    var runHistory: HashMap<String, RunDescription>? = hashMapOf();
}
