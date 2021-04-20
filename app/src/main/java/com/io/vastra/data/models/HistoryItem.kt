package com.io.vastra.data.models

import java.sql.Time
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class HistoryItem constructor(val duration: Duration, val distance: Int, val date: Date) {
}