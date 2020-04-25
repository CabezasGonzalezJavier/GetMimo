package com.a.getmimo.data.source.local

import com.a.getmimo.data.source.toRoomLesson
import com.a.getmimo.domain.entity.SimpleLesson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface LocalDataSource {

    suspend fun saveLesson(simple: SimpleLesson)
}

class RoomDataSource(db: MimoDatabase) : LocalDataSource {

    private val dao: LessonDao = db.lessonDao()

    override suspend fun saveLesson(simple: SimpleLesson) {
        withContext(
            Dispatchers.IO
        ) {
            dao.insertLesson(simple.toRoomLesson())
        }
    }

}