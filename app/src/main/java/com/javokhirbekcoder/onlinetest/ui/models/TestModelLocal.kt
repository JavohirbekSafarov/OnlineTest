package com.javokhirbekcoder.onlinetest.ui.models

/*
Created by Javokhirbek on 05/12/2023 at 11:41
*/

data class TestModelLocal(
    val answer: String,
    val answer_count: Int,
    val id: Int,
    val question_path: String,
    val subject_id: Int,
    val status: Int
)
