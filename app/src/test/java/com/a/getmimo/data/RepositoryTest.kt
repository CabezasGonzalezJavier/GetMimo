package com.a.getmimo.data

import com.a.getmimo.data.source.local.LocalDataSource
import com.a.getmimo.data.source.remote.RemoteDataSource
import com.a.getmimo.simpleLessonMocked
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @Mock
    lateinit var remoteDataSource: RemoteDataSource
    @Mock
    lateinit var localDataSource: LocalDataSource

    lateinit var repository: Repository

    @Before
    fun setup() {
        repository = Repository(remoteDataSource, localDataSource)
    }

    @Test
    fun `getting lesson`() {
        runBlocking {
            repository.getLesson()

            verify(remoteDataSource).getLessons()
        }
    }

    @Test
    fun `saving lesson`(){
        runBlocking {
            repository.saveLesson(simpleLessonMocked)

            verify(localDataSource).saveLesson(simpleLessonMocked)
        }
    }

}