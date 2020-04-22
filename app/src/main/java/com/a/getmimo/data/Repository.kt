package com.a.getmimo.data

import com.a.getmimo.data.source.LocalDataSource
import com.a.getmimo.data.source.RemoteDataSource
import com.a.getmimo.data.source.networking.Resource
import com.a.getmimo.domain.Lesson

class Repository (var remoteDataSource: RemoteDataSource, val localDataSource: LocalDataSource) {

    suspend fun getLesson(): Resource<List<Lesson>> = remoteDataSource.getLessons()
}