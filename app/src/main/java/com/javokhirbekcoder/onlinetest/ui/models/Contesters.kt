package com.javokhirbekcoder.onlinetest.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.javokhirbekcoder.onlinetest.room.DatabaseConfig

@Entity(DatabaseConfig.CONTESTER_TABLE)
data class Contesters(
    @PrimaryKey
    val id: Int,
    var answers: String,
    val blank_path: String,
    val contest_id: Int,
    val course: Int,
    val faculty: String,
    val group: String,
    val guid: String,
    val name: String,
    val speciality: String
)