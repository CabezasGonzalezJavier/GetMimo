package com.a.getmimo.domain.usecases

import com.a.getmimo.data.RepositoryInterface
import com.a.getmimo.domain.entity.SimpleLesson

class SaveLesson(var repositoryInterface: RepositoryInterface) {

    suspend fun saveLesson(simpleLesson: SimpleLesson): SimpleLesson = with(simpleLesson) {
        also {
            repositoryInterface.saveLesson(simpleLesson)
        }
    }
}