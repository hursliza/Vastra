package com.io.vastra.data

import java.sql.Time
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

data class HistoryItem constructor(val duration: Time, val distance: String, val date: Date) {
}