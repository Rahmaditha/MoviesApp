package com.cookiss.moviesapp.util

import retrofit2.HttpException
import java.net.SocketTimeoutException

//typealias SimpleResource = id.obeng.mitra.core.data.Resource<Unit>

//sealed class Resource<T>(val data: T? = null, val message: String? = null) {
//    class Success<T>(data: T) : Resource<T>(data)
//    class Loading<T>(data: T? = null) : Resource<T>(data)
//    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
//}

typealias SimpleResource = Resource<Unit>

sealed class Resource <out T> (val _exception: Exception?, val status : Status, val _data : T?, val message: String?) {

    data class Success<out R>(val data : R) : Resource<R>(
        status = Status.SUCCESS,
        _data = data,
        message = null,
        _exception = null
    )
    data class Loading(val isLoading : Boolean) : Resource<Nothing>(
        status = Status.LOADING,
        _data = null,
        message = null,
        _exception = null
    )
    data class Error(val exceptionn: Exception?, val exception: String) : Resource<Nothing>(
        status = Status.ERROR,
        _data = null,
        message = exception,
        _exception = exceptionn
    )
}
