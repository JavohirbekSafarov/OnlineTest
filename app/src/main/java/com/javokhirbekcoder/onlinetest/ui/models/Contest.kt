package com.javokhirbekcoder.onlinetest.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.javokhirbekcoder.onlinetest.room.DatabaseConfig

@Entity(DatabaseConfig.CONTEST_TABLE)
data class Contest(
    @PrimaryKey
    val id: Int,
    val contest_type_id: Int,
    val duration: Int,
    val end_date: String,
    var max_ball: Int,
    val start_date: String,
    val subject_id: Int,
    val test_count: Int,
    val tests: String
)