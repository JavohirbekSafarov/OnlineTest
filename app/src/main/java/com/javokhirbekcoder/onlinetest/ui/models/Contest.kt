package com.javokhirbekcoder.onlinetest.ui.models

data class Contest(
    val contest_type_id: Int,
    val duration: Int,
    val end_date: String,
    val id: Int,
    val max_ball: Int,
    val start_date: String,
    val subject_id: Int,
    val test_count: Int,
    val tests: String
)