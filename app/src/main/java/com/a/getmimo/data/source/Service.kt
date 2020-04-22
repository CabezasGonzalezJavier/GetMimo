package com.a.getmimo.data.source

import com.a.getmimo.data.source.remote.Response
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface Service {

    @GET("lessons")
    fun getLessonsAsync(): Deferred<Response>
}
