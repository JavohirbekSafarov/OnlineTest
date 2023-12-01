package com.javokhirbekcoder.onlinetest.ui.viewModels

import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.onlinetest.ui.models.LoginDataModel
import com.javokhirbekcoder.onlinetest.ui.repository.MainRepository
import com.javokhirbekcoder.onlinetest.utils.NetworkStateListener
import com.javokhirbekcoder.onlinetest.utils.SharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
Created by Javokhirbek on 29/11/2023 at 07:11
*/
@HiltViewModel
class LoginFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkStateListener: NetworkStateListener,
    private val sharedPrefs: SharedPrefs
) : ViewModel() {
    val networkState = networkStateListener
    fun getSubjects() = mainRepository.getSubjects()
    fun getSubjectsList() = mainRepository.subjectsList
    fun enterTest(guid: String, sub_id: Int) = mainRepository.enterTest(guid, sub_id)
    //fun getTestData() = mainRepository.enterTestObj
    fun setDataToShared(guid: String, sub_id: Int) {
        sharedPrefs.setGuid(guid)
        sharedPrefs.setSubId(sub_id)
    }
    fun getDataFromShared(): LoginDataModel {
        val guid = sharedPrefs.getGuid()
        val id = sharedPrefs.getSubId()
        return LoginDataModel(guid, id)
    }
}