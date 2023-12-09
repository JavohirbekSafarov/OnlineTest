package com.javokhirbekcoder.onlinetest.ui.viewModels

import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.onlinetest.ui.models.AnswerModel
import com.javokhirbekcoder.onlinetest.ui.models.Contesters
import com.javokhirbekcoder.onlinetest.ui.models.EnterTestModel
import com.javokhirbekcoder.onlinetest.ui.models.LoginDataModel
import com.javokhirbekcoder.onlinetest.ui.models.TestModel
import com.javokhirbekcoder.onlinetest.ui.models.TestModelLocal
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

    fun getSubjects() = mainRepository.getSubjectsList()
    fun reloadSubjects() = mainRepository.getSubjects()
    fun enterTest(guid:String, id:Int) = mainRepository.enterTest(guid, id)
    fun getTest(id:Int) = mainRepository.getTest(id)
    fun addTestLocal(testModel: TestModelLocal) = mainRepository.addTestLocal(testModel)
    fun getTestsLocal() = mainRepository.getTestLocal()
    fun deleteTestLocal() = mainRepository.deleteTestsLocal()
    fun addAnswerLocal(answerModel: AnswerModel) = mainRepository.addAnswerLocal(answerModel)
    fun getAnswersLocal() = mainRepository.getAnswerLocal()
    suspend fun loadAnswersDatabase() = mainRepository.getAnswersFromDatabase()
    suspend fun addAnswersDatabase() = mainRepository.saveAnswersToDatabase()
    suspend fun deleteAnswersDatabase() = mainRepository.deleteAnswersFromDatabase()

    fun deleteAnswerLocal() = mainRepository.deleteAnswersLocal()
    fun submitTest(contesters: Contesters) = mainRepository.submitTest(contesters)
    fun getContestType(id: Int) = mainRepository.getContestType(id)

    suspend fun getEnterTestModel() = mainRepository.getEnterTestModel()
    suspend fun saveEnterTestModel(enterTestModel: EnterTestModel) = mainRepository.saveEnterTestModel(enterTestModel)
}