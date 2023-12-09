package com.javokhirbekcoder.onlinetest.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.javokhirbekcoder.onlinetest.ui.models.AnswerModel
import com.javokhirbekcoder.onlinetest.ui.models.Contest
import com.javokhirbekcoder.onlinetest.ui.models.Contesters

/*
Created by Javokhirbek on 01/12/2023 at 20:24
*/
@Dao
interface Dao {

    @Query("SELECT * FROM ${DatabaseConfig.ANSWERS_TABLE}")
    suspend fun getAnswers(): List<AnswerModel>

    @Query("SELECT * FROM ${DatabaseConfig.CONTEST_TABLE}")
    suspend fun getContests(): Contest

    @Query("SELECT * FROM ${DatabaseConfig.CONTESTER_TABLE}")
    suspend fun getContesters(): Contesters

    @Insert
    suspend fun insertAnswers(answerModel: AnswerModel)

    @Insert
    suspend fun insertContesters(contesters: Contesters)

    @Insert
    suspend fun insertContests(contest: Contest)

    @Query("DELETE FROM ${DatabaseConfig.ANSWERS_TABLE}")
    suspend fun deleteAnswers()

    @Query("DELETE FROM ${DatabaseConfig.CONTEST_TABLE}")
    suspend fun deleteContests()

    @Query("DELETE FROM ${DatabaseConfig.CONTESTER_TABLE}")
    suspend fun deleteContesters()
}