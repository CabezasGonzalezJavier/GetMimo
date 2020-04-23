package com.a.getmimo

import com.a.getmimo.data.source.LocalDataSource
import com.a.getmimo.data.source.RemoteDataSource
import com.a.getmimo.domain.entity.Content
import com.a.getmimo.domain.entity.Lesson
import com.a.getmimo.domain.entity.networking.Resource
import com.a.getmimo.domain.entity.networking.Status
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initMockedDi(vararg modules: Module) {
    startKoin {
        modules(listOf(mockedAppModule, dataModule, useCasesModule) + modules)
    }
}

private val mockedAppModule = module {
    single<LocalDataSource> { FakeLocalDataSource() }
    single<RemoteDataSource> { FakeRemoteDataSource() }
    single { Dispatchers.Unconfined }
}

val content = Content("color", "text")
val contentList = listOf(content.copy())
val defaultLesson= Lesson(1, contentList, 0, 0)
val oneLesson = listOf(defaultLesson.copy())
val defaultResource = Resource(Status.SUCCESS, oneLesson, "")


val emptyLessons = listOf<Lesson>()
val emptyResource = Resource(Status.SUCCESS, emptyLessons, "")

class FakeLocalDataSource : LocalDataSource {



}

class FakeRemoteDataSource : RemoteDataSource {

    override suspend fun getLessons(): Resource<List<Lesson>> = defaultResource

}