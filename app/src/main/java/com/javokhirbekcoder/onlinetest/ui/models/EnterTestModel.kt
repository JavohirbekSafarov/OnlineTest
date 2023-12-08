package com.javokhirbekcoder.onlinetest.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.javokhirbekcoder.onlinetest.room.DatabaseConfig

data class EnterTestModel(
    var contest: Contest,
    val contesters: Contesters
)