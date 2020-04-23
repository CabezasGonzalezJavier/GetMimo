package com.a.getmimo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.a.getmimo.data.source.networking.Resource
import com.a.getmimo.data.source.networking.Status
import com.a.getmimo.domain.entity.Content
import com.a.getmimo.domain.entity.Lesson
import com.a.getmimo.domain.usecases.GetLessons
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getLessons: GetLessons
    @Mock
    lateinit var observer: Observer<MainViewModel.UiModel>

    lateinit var mainViewModel: MainViewModel


    @Before
    fun setup() {

        mainViewModel = MainViewModel(getLessons, Dispatchers.Unconfined)
    }

    @Test
    fun `refreshing content`(){
        mainViewModel.model.observeForever(observer)

        verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
    }

    @Test
    fun `launching loading`() {
        mainViewModel.model.observeForever(observer)
        mainViewModel.myLaunch()

        verify(observer).onChanged(MainViewModel.UiModel.Loading)
    }

    @Test
    fun `launching empty content`() {
        runBlocking {
            val emptyLessons = listOf<Lesson>()
            val emptyResource = Resource(Status.SUCCESS, emptyLessons, "")


            whenever(getLessons.invoke()).thenReturn(emptyResource)
            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            val lessons = getLessons.invoke().data

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.ShowEmptyData)
            assertEquals(lessons, emptyResource.data)
        }
    }

    @Test
    fun `launching content`() {
        runBlocking {
            val content = Content("color", "text")
            val contentList = listOf(content.copy())
            val defaultLesson= Lesson(1, contentList, 0, 0)
            val oneLesson = listOf(defaultLesson.copy())
            val resource = Resource(Status.SUCCESS, oneLesson, "")

            whenever(getLessons.invoke()).thenReturn(resource)
            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            val lessons = getLessons.invoke().data

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.Content(resource.data))
            assertEquals(lessons, resource.data)
        }
    }

    @Test
    fun `launching null content with success status`() {
        runBlocking {

            val resource = Resource(Status.SUCCESS, null, "")

            whenever(getLessons.invoke()).thenReturn(resource)
            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            val lessons = getLessons.invoke().data

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.ShowErrorCall)
            assertEquals(lessons, resource.data)
        }
    }

    @Test
    fun `launching null content with error status`() {
        runBlocking {

            val resource = Resource(Status.ERROR, null, "")

            whenever(getLessons.invoke()).thenReturn(resource)
            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            val lessons = getLessons.invoke().data

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.ShowErrorCall)
            assertEquals(lessons, resource.data)
        }
    }

    @Test
    fun `getting lessons without internet`() {
        mainViewModel.model.observeForever(observer)
        mainViewModel.checkInternet(false)

        verify(observer).onChanged(MainViewModel.UiModel.ShowCanCheckYourInternet)
    }

    @Test
    fun `getting lessons`() {
        mainViewModel.model.observeForever(observer)
        mainViewModel.checkInternet(true)

        verify(observer).onChanged(MainViewModel.UiModel.Loading)
    }
}