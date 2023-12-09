package com.javokhirbekcoder.onlinetest.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.javokhirbekcoder.onlinetest.room.DatabaseConfig

/*
Created by Javokhirbek on 05/12/2023 at 12:20
*/
@Entity(DatabaseConfig.ANSWERS_TABLE)
data class AnswerModel(
    @PrimaryKey
    val id:Int,
    val correctAnswer: String,
    var selectedAnswer:String
)