package com.a.getmimo.data.source

import com.a.getmimo.domain.entity.networking.Resource
import com.a.getmimo.domain.entity.networking.ResponseHandler
import com.a.getmimo.domain.entity.Lesson

interface RemoteDataSource {

    suspend fun getLessons(): Resource<List<Lesson>>
}

class MimoDataSource(private val mimoDB: MimoDB,
                     private val responseHandler: ResponseHandler
) : RemoteDataSource {

    override suspend fun getLessons(): Resource<List<Lesson>> {
        return try {
            val response = mimoDB.service.getLessonsAsync().await().lessons.map{
                it.toDomainLesson()
            }
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

}
