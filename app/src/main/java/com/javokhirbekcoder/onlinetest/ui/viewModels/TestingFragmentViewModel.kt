package com.javokhirbekcoder.onlinetest.ui.viewModels

import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.onlinetest.ui.models.LoginDataModel
import com.javokhirbekcoder.onlinetest.ui.repository.MainRepository
import com.javokhirbekcoder.onlinetest.utils.NetworkStateListener
import com.javokhirbekcoder.onlinetest.utils.SharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
Created by Javokhirbek on 29/11/2023 at 14:31
*/

@HiltViewModel
class TestingFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkStateListener: NetworkStateListener,
    private val sharedPrefs: SharedPrefs
) :ViewModel() {
    val networkState = networkStateListener
    fun getDataFromShared(): LoginDataModel {
        val guid = sharedPrefs.getGuid()
        val id = sharedPrefs.getSubId()
        return LoginDataModel(guid, id)
    }
    fun enterTest(guid:String, id:Int) = mainRepository.enterTest(guid, id)
    fun getTest(id:Int) = mainRepository.getTest(id)
}