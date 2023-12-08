package com.javokhirbekcoder.onlinetest.ui.repository

import androidx.lifecycle.MutableLiveData
import com.javokhirbekcoder.onlinetest.api.ApiService
import com.javokhirbekcoder.onlinetest.room.AppDatabase
import com.javokhirbekcoder.onlinetest.ui.models.AnswerModel
import com.javokhirbekcoder.onlinetest.ui.models.Contesters
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModelNullable
import com.javokhirbekcoder.onlinetest.ui.models.ResponseModel
import com.javokhirbekcoder.onlinetest.ui.models.Subjects
import com.javokhirbekcoder.onlinetest.ui.models.TestModel
import com.javokhirbekcoder.onlinetest.ui.models.TestModelLocal
import com.javokhirbekcoder.onlinetest.utils.NetworkResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
Created by Javokhirbek on 28/11/2023 at 09:43
*/

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase
) {

    val subjectsList = MutableLiveData<NetworkResult<Subjects>>()
    private val tests = ArrayList<TestModelLocal>()
    private val answers = ArrayList<AnswerModel>()

    fun addTestLocal(testModel: TestModelLocal) {
        tests.add(testModel)
    }

    fun getTestLocal(): ArrayList<TestModelLocal> = tests

    fun deleteTestsLocal() {
        tests.clear()
    }

    fun addAnswerLocal(answerModel: AnswerModel) {
        answers.forEach {
            if (it.id == answerModel.id) {
                return
            }
        }
        answers.add(answerModel)
    }

    fun getAnswerLocal(): ArrayList<AnswerModel> = answers

    fun deleteAnswersLocal() {
        answers.clear()
    }

    /*   fun getApi(): MutableLiveData<String> {
           val myString = MutableLiveData<String>()
           apiService.api().enqueue(object : Callback<ResponseModel> {
               override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                   if (response.code() == 200) {
                       myString.postValue(response.body().toString())
                   }
               }

               override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                   Log.d("Response Error", t.toString())
               }
           })
           return myString
       }*/

    fun getSubjects(): MutableLiveData<NetworkResult<Subjects>> {

        subjectsList.postValue(NetworkResult.loading())

        apiService.getSubjects().enqueue(object : Callback<Subjects> {
            override fun onResponse(call: Call<Subjects>, response: Response<Subjects>) {
                if (response.code() == 200) {
                    subjectsList.postValue(
                        NetworkResult.success(response.body())
                    )
                }
            }

            override fun onFailure(call: Call<Subjects>, t: Throwable) {
                subjectsList.postValue(NetworkResult.error(t.toString()))
            }
        })
        return subjectsList
    }

    fun enterTest(guid: String, subjectId: Int): MutableLiveData<NetworkResult<EnterTestModel>> {
        val enterTestObj = MutableLiveData<NetworkResult<EnterTestModel>>()
        enterTestObj.postValue(NetworkResult.loading())

        apiService.getTestData(guid, subjectId).enqueue(object : Callback<EnterTestModel> {
            override fun onResponse(
                call: Call<EnterTestModel>,
                response: Response<EnterTestModel>
            ) {
                if (response.code() == 200)
                    enterTestObj.postValue(NetworkResult.success(response.body()))
                else if (response.code() == 404)
                    enterTestObj.postValue(NetworkResult.error("Malumot notog'ri kiritilgan!"))
                else
                    enterTestObj.postValue(NetworkResult.error(response.body().toString()))
            }

            override fun onFailure(call: Call<EnterTestModel>, t: Throwable) {
                enterTestObj.postValue(NetworkResult.error(t.toString()))
            }
        })

        return enterTestObj
    }

    fun getTest(id: Int): MutableLiveData<NetworkResult<TestModel>> {
        val test = MutableLiveData<NetworkResult<TestModel>>()
        test.postValue(NetworkResult.loading())

        apiService.getTest(id).enqueue(object : Callback<TestModel> {
            override fun onResponse(call: Call<TestModel>, response: Response<TestModel>) {
                if (response.code() == 200) {
                    test.postValue(NetworkResult.success(response.body()))
                } else if (response.code() == 404) {
                    test.postValue(NetworkResult.error("Test topilmadi!"))
                } else {
                    test.postValue(NetworkResult.error(response.body().toString()))
                }
            }

            override fun onFailure(call: Call<TestModel>, t: Throwable) {
                test.postValue(NetworkResult.error(t.toString()))
            }

        })

        return test
    }

    fun submitTest(contesters: Contesters): MutableLiveData<NetworkResult<ResponseModel>> {
        val mresponse = MutableLiveData<NetworkResult<ResponseModel>>()
        mresponse.postValue(NetworkResult.loading())

        apiService.submitAnswer(contesters).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == 200) {
                    mresponse.postValue(NetworkResult.success(response.body()))
                } else if (response.code() == 404) {
                    mresponse.postValue(NetworkResult.error("O`quvchi topilmadi!"))
                } else {
                    mresponse.postValue(NetworkResult.error(response.body().toString()))
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                mresponse.postValue(NetworkResult.error(t.toString()))
            }
        })

        return mresponse
    }

    suspend fun saveEnterTestModel(enterTestModel: EnterTestModel) {
        deleteEnterTestModel()
        database.Dao().insertContests(enterTestModel.contest)
        database.Dao().insertContesters(enterTestModel.contesters)
    }

    suspend fun getEnterTestModel(): EnterTestModelNullable {
        return EnterTestModelNullable(
            database.Dao().getContests(),
            database.Dao().getContesters()
        )
    }

    suspend fun deleteEnterTestModel() {
        database.Dao().deleteContests()
        database.Dao().deleteContesters()
    }
}