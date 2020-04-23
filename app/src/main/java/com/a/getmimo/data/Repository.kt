package com.a.getmimo.data

import com.a.getmimo.data.source.LocalDataSource
import com.a.getmimo.data.source.RemoteDataSource
import com.a.getmimo.domain.entity.networking.Resource
import com.a.getmimo.domain.entity.Lesson

interface RepositoryInterface {
    suspend fun getLesson(): Resource<List<Lesson>>
}

class Repository(var remoteDataSource: RemoteDataSource, val localDataSource: LocalDataSource) :
    RepositoryInterface {

    override suspend fun getLesson(): Resource<List<Lesson>> = remoteDataSource.getLessons()
}