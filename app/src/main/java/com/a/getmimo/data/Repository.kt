package com.a.getmimo.data

import com.a.getmimo.data.source.local.LocalDataSource
import com.a.getmimo.data.source.remote.RemoteDataSource
import com.a.getmimo.domain.entity.networking.Resource
import com.a.getmimo.domain.entity.Lesson
import com.a.getmimo.domain.entity.SimpleLesson

interface RepositoryInterface {

    suspend fun getLesson(): Resource<List<Lesson>>

    suspend fun saveLesson(simpleLesson: SimpleLesson)
}

class Repository(var remoteDataSource: RemoteDataSource, val localDataSource: LocalDataSource) :
    RepositoryInterface {

    override suspend fun getLesson(): Resource<List<Lesson>> =
        remoteDataSource.getLessons()

    override suspend fun saveLesson(simpleLesson: SimpleLesson) =
        localDataSource.saveLesson(simpleLesson)

}