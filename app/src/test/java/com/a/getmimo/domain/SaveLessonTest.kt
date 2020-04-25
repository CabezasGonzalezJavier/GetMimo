package com.a.getmimo.domain

import com.a.getmimo.data.Repository
import com.a.getmimo.domain.usecases.SaveLesson
import com.a.getmimo.simpleLessonMocked
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveLessonTest {

    @Mock
    lateinit var repository: Repository

    lateinit var saveLesson: SaveLesson

    @Before
    fun setup() {
        saveLesson = SaveLesson(repository)
    }

    @Test
    fun `saving lesson`() {
        runBlocking {

            saveLesson.invoke(simpleLessonMocked)

            verify(repository).saveLesson(simpleLessonMocked)
        }
    }
}