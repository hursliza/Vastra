package com.io.vastra.data.models

import java.sql.Time
import java.util.*

data class HistoryItem constructor(val duration: Time, val distance: String, val date: Date) {
}