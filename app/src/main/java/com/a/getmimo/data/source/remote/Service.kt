package com.a.getmimo.data.source.remote

import com.a.getmimo.data.source.remote.model.Response
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface Service {

    @GET("lessons")
    fun getLessonsAsync(): Deferred<Response>
}
