package com.example.todoapp.utils

import android.os.Message

sealed class Resources<T>(val status : Status, val data : T? = null, val message: String ?= null) {

     class Success <T>(data: T?) : Resources<T>(Status.SUCCESS,data)
     class Error <T>(message: String? , data: T? = null) : Resources<T>(Status.ERROR,data,message)
     class Loading <T>: Resources<T>(Status.LOADING)



}