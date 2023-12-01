package com.javokhirbekcoder.onlinetest.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.CreationExtras
import com.javokhirbekcoder.onlinetest.api.ApiService
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel
import com.javokhirbekcoder.onlinetest.ui.models.ResponceModel
import com.javokhirbekcoder.onlinetest.ui.models.Subjects
import com.javokhirbekcoder.onlinetest.ui.models.SubjectsItem
import com.javokhirbekcoder.onlinetest.ui.models.TestModel
import com.javokhirbekcoder.onlinetest.utils.NetworkResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
Created by Javokhirbek on 28/11/2023 at 09:43
*/

class MainRepository @Inject constructor(
    private val apiService: ApiService
) {

    val subjectsList = MutableLiveData<NetworkResult<Subjects>>()


 /*   fun getApi(): MutableLiveData<String> {
        val myString = MutableLiveData<String>()
        apiService.api().enqueue(object : Callback<ResponceModel> {
            override fun onResponse(call: Call<ResponceModel>, response: Response<ResponceModel>) {
                if (response.code() == 200) {
                    myString.postValue(response.body().toString())
                }
            }

            override fun onFailure(call: Call<ResponceModel>, t: Throwable) {
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

    fun enterTest(guid:String, subjectId:Int): MutableLiveData<NetworkResult<EnterTestModel>> {
        val enterTestObj = MutableLiveData<NetworkResult<EnterTestModel>>()
        enterTestObj.postValue(NetworkResult.loading())

        apiService.getTestData(guid, subjectId).enqueue(object :Callback<EnterTestModel>{
            override fun onResponse(
                call: Call<EnterTestModel>,
                response: Response<EnterTestModel>
            ) {
                if (response.code() == 200)
                    enterTestObj.postValue(NetworkResult.success(response.body()))
                else  if (response.code() == 404)
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

    fun getTest(id:Int): MutableLiveData<NetworkResult<TestModel>>{
        val test = MutableLiveData<NetworkResult<TestModel>>()
        test.postValue(NetworkResult.loading())

        apiService.getTest(id).enqueue(object :Callback<TestModel>{
            override fun onResponse(call: Call<TestModel>, response: Response<TestModel>) {
                if (response.code() == 200){
                    test.postValue(NetworkResult.success(response.body()))
                }else if(response.code() == 404){
                    test.postValue(NetworkResult.error("Test topilmadi!"))
                }else {
                    test.postValue(NetworkResult.error(response.body().toString()))
                }
            }

            override fun onFailure(call: Call<TestModel>, t: Throwable) {
                test.postValue(NetworkResult.error(t.toString()))
            }

        })

        return test
    }
}