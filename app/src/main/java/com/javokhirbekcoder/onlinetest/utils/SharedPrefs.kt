package com.javokhirbekcoder.onlinetest.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

/*
Created by Javokhirbek on 29.11.2023 at 14:42
*/

class SharedPrefs(context: Context) {

    private var sharedPrefs: SharedPreferences
    private var editor: Editor

    private val GUID = "guid"
    private val SUB_ID = "sub_id"

    init {
        sharedPrefs = context.getSharedPreferences("appConfig", Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()
    }

    fun setGuid(guid: String){
        editor.putString(GUID, guid)
        editor.apply()
    }

    fun getGuid():String?{
        return sharedPrefs.getString(GUID, "")
    }

    fun setSubId(id:Int){
        editor.putInt(SUB_ID, id)
        editor.apply()
    }

    fun getSubId(): Int {
        return sharedPrefs.getInt(SUB_ID, -1)
    }

//    fun setLoginData(user: User) {
//        editor.putString(LOGIN_DATA_USER, user.username)
//        writePassToSharedPrefs(user.password)
//        editor.apply()
//    }
//
//    fun getLoginData(): User? {
//        try {
//            if (getLogged()){
//                val username = sharedPrefs.getString(LOGIN_DATA_USER, "")!!
//                val password = readPassFromSharedPrefs()!!
//                return User(username, password)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
//    private fun writePassToSharedPrefs(value: String) {
//        with (sharedPrefsForPass.edit()) {
//            putString(LOGIN_DATA_PASS, value)
//            apply()
//        }
//    }
//
//    private fun readPassFromSharedPrefs(): String? {
//        return sharedPrefsForPass.getString(LOGIN_DATA_PASS, "")
//    }
}