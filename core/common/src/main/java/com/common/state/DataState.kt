package com.common.state

sealed class DataState <out T> {
    data class Success<out T>(val data: T): DataState<T>()
    data object Idle: DataState<Nothing>()
}