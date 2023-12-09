package com.javokhirbekcoder.onlinetest.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.javokhirbekcoder.onlinetest.ui.models.AnswerModel
import com.javokhirbekcoder.onlinetest.ui.models.Contest
import com.javokhirbekcoder.onlinetest.ui.models.Contesters

/*
Created by Javokhirbek on 01/12/2023 at 20:26
*/

@Database(entities = [Contest::class, Contesters::class, AnswerModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun Dao():Dao
}