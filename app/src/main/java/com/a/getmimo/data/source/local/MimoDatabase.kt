package com.a.getmimo.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.a.getmimo.data.source.local.model.Lesson

@Database(entities = [Lesson::class],version = 1)
abstract class MimoDatabase : RoomDatabase() {
    abstract fun lessonDao() : LessonDao

    companion object {

        private var INSTANCE: MimoDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): MimoDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MimoDatabase::class.java, "Lesson.db"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }
}