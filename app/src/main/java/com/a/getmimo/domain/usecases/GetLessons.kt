package com.a.getmimo.domain.usecases

import com.a.getmimo.data.RepositoryInterface
import com.a.getmimo.domain.entity.networking.Resource
import com.a.getmimo.domain.entity.Lesson

class GetLessons(private val repository: RepositoryInterface) {
    suspend fun invoke(): Resource<List<Lesson>> = repository.getLesson()
}