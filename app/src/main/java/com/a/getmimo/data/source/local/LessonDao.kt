package com.a.getmimo.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.a.getmimo.data.source.local.model.Lesson

@Dao
interface LessonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLesson(lesson: Lesson)
}