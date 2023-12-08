package com.javokhirbekcoder.onlinetest.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.javokhirbekcoder.onlinetest.ui.models.Contest
import com.javokhirbekcoder.onlinetest.ui.models.Contesters
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel

/*
Created by Javokhirbek on 01/12/2023 at 20:24
*/
@Dao
interface Dao {
    @Insert
    suspend fun insertContests(contest: Contest)

    @Query("SELECT * FROM contest")
    suspend fun getContests(): Contest

    @Insert
    suspend fun insertContesters(contesters: Contesters)

    @Query("SELECT * FROM contesters")
    suspend fun getContesters(): Contesters

    @Query("DELETE FROM contest")
    suspend fun deleteContests()

    @Query("DELETE FROM contesters")
    suspend fun deleteContesters()
}