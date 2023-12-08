package com.javokhirbekcoder.onlinetest.api

import com.javokhirbekcoder.onlinetest.ui.models.Contesters
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel
import com.javokhirbekcoder.onlinetest.ui.models.ResponseModel
import com.javokhirbekcoder.onlinetest.ui.models.Subjects
import com.javokhirbekcoder.onlinetest.ui.models.TestModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

/*
Created by Javokhirbek on 28/11/2023 at 09:59
*/

interface ApiService {
//    @GET("votes")
//    fun getAllVotes(): Call<Votes>

    @GET("/")
    fun api(): Call<ResponseModel>

    @GET("/subjects")
    fun getSubjects(): Call<Subjects>

    @GET("/enter_test")
    fun getTestData(
        @Query("guid") guid: String,
        @Query("subject_id") subjectId: Int
    ): Call<EnterTestModel>

    @GET("/test")
    fun getTest(
        @Query("_id") id:Int
    ):Call<TestModel>

    @PUT("/submit_answer")
    fun submitAnswer(
        @Body contesters: Contesters
    ):Call<ResponseModel>
}