package com.javokhirbekcoder.onlinetest.utils

/*
Created by Javokhirbek on 28/11/2023 at 10:39
*/

data class NetworkResult<out T>(
    val status: NetworkStatus,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> loading(): NetworkResult<T> {
            return NetworkResult(NetworkStatus.LOADING, null, null)
        }

        fun <T> success(data: T?): NetworkResult<T> {
            return NetworkResult(NetworkStatus.SUCCESS, data, null)
        }

        fun <T> error(message: String?): NetworkResult<T> {
            return NetworkResult(NetworkStatus.ERROR, null, message)
        }
    }
}
