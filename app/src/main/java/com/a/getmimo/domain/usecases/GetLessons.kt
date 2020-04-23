package com.a.getmimo.domain.usecases

import com.a.getmimo.data.Repository
import com.a.getmimo.data.source.networking.Resource
import com.a.getmimo.domain.entity.Lesson

class GetLessons(private val repository: Repository) {
    suspend fun invoke(): Resource<List<Lesson>> = repository.getLesson()
}