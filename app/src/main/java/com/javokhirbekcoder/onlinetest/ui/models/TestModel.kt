package com.javokhirbekcoder.onlinetest.ui.models

data class TestModel(
    val answer: String,
    val answer_count: Int,
    val id: Int,
    val question_path: String,
    val subject_id: Int
)