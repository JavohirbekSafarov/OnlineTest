package com.javokhirbekcoder.onlinetest.ui.viewModels

import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.onlinetest.ui.repository.MainRepository
import com.javokhirbekcoder.onlinetest.utils.NetworkStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
Created by Javokhirbek on 28/11/2023 at 10:04
*/
@HiltViewModel
class SplashFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkStateListener: NetworkStateListener
) :ViewModel(){

    val networkState = networkStateListener
    fun getSubjects() = mainRepository.getSubjects()
}
