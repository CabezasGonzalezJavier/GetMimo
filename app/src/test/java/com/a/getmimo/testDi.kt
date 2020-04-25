package com.a.getmimo

import com.a.getmimo.data.source.local.LocalDataSource
import com.a.getmimo.data.source.remote.RemoteDataSource
import com.a.getmimo.domain.entity.Content
import com.a.getmimo.domain.entity.Lesson
import com.a.getmimo.domain.entity.SimpleLesson
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

val content = Content("color", "texttexttext")
val contentList = listOf(content.copy())
val defaultLesson= Lesson(1, contentList, 4, 8)
val oneLesson = listOf(defaultLesson.copy())
val defaultResource = Resource(Status.SUCCESS, oneLesson, "")


val emptyLessons = listOf<Lesson>()
val emptyResource = Resource(Status.SUCCESS, emptyLessons, "")

val threeContent = Content("color", "text")
val threeContentList = listOf(threeContent.copy(),threeContent.copy(),threeContent.copy())
val threeDefaultLesson= Lesson(1, threeContentList, 0, 0)
val threeOneLesson = listOf(threeDefaultLesson.copy())
val threeResource = Resource(Status.SUCCESS, threeOneLesson, "")

val simpleLessonMocked = SimpleLesson(1577836800000L, 1577836820000L, 9)

class FakeLocalDataSource : LocalDataSource {
    override suspend fun saveLesson(simple: SimpleLesson) {}


}

class FakeRemoteDataSource : RemoteDataSource {

    override suspend fun getLessons(): Resource<List<Lesson>> = defaultResource

}