package com.a.getmimo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.a.getmimo.defaultResource
import com.a.getmimo.domain.entity.networking.Resource
import com.a.getmimo.domain.entity.networking.Status
import com.a.getmimo.domain.usecases.GetLessons
import com.a.getmimo.emptyResource
import com.a.getmimo.threeResource
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

    @Test
    fun `launching one content`() {
        runBlocking {


            whenever(getLessons.invoke()).thenReturn(defaultResource)
            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            val lessons = getLessons.invoke().data

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.Loading)
            verify(observer).onChanged(MainViewModel.UiModel.ShowFirstText("text"))
            assertEquals(lessons, defaultResource.data!!)
        }
    }

    @Test
    fun `launching three content`() {
        runBlocking {

            whenever(getLessons.invoke()).thenReturn(threeResource)
            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            val lessons = getLessons.invoke().data

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.Loading)
            verify(observer).onChanged(MainViewModel.UiModel.ShowFirstText("text"))
            verify(observer).onChanged(MainViewModel.UiModel.ShowSecondText("text"))
            assertEquals(lessons, threeResource.data!!)
        }
    }

    @Test
    fun `checking solution`(){
        runBlocking {

            whenever(getLessons.invoke()).thenReturn(threeResource)
            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            val lessons = getLessons.invoke().data
        mainViewModel.checkSolution("")

        verify(observer).onChanged(MainViewModel.UiModel.ShowEmptyInput)
    }}

    @Test
    fun `checking ok solution`(){
        runBlocking {

        whenever(getLessons.invoke()).thenReturn(threeResource)
        mainViewModel.model.observeForever(observer)
        mainViewModel.myLaunch()
        val lessons = getLessons.invoke().data
        mainViewModel.checkSolution("text")

        verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
        verify(observer).onChanged(MainViewModel.UiModel.Loading)
        verify(observer).onChanged(MainViewModel.UiModel.ShowFirstText("text"))
        verify(observer).onChanged(MainViewModel.UiModel.ShowSecondText("text"))
        assertEquals(lessons, threeResource.data!!)
        verify(observer).onChanged(MainViewModel.UiModel.EnableButton)
        }
    }

    @Test
    fun `checking wrong solution`(){
        runBlocking {

            whenever(getLessons.invoke()).thenReturn(threeResource)
            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            val lessons = getLessons.invoke().data
            mainViewModel.checkSolution("tet")

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.Loading)
            verify(observer).onChanged(MainViewModel.UiModel.ShowFirstText("text"))
            verify(observer).onChanged(MainViewModel.UiModel.ShowSecondText("text"))
            assertEquals(lessons, threeResource.data!!)
            verify(observer).onChanged(MainViewModel.UiModel.DisableButton)
        }
    }
}