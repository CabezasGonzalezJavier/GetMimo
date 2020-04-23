package com.a.getmimo.domain

import com.a.getmimo.data.Repository
import com.a.getmimo.domain.usecases.GetLessons
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetLessonsTest {

    @Mock
    lateinit var repository: Repository

    lateinit var getLessons: GetLessons

    @Before
    fun setup() {
        getLessons = GetLessons(repository)
    }

    @Test
    fun `getting lessons`() {
        runBlocking {

            getLessons.invoke()
            verify(repository).getLesson()
        }

    }
}