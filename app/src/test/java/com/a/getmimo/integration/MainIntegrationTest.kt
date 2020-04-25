package com.a.getmimo.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.a.getmimo.FakeRemoteDataSource
import com.a.getmimo.data.source.remote.RemoteDataSource
import com.a.getmimo.initMockedDi
import com.a.getmimo.ui.MainViewModel
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainIntegrationTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<MainViewModel.UiModel>

    lateinit var mainViewModel: MainViewModel
    private lateinit var remoteDataSource: FakeRemoteDataSource

    @Before
    fun setup() {
        val viewModelModule = module {
            factory { MainViewModel(get(), get()) }
        }
        initMockedDi(viewModelModule)
        mainViewModel = get()

        remoteDataSource = get<RemoteDataSource>() as FakeRemoteDataSource
    }

    @Test
    fun `refreshing content`() {
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
    fun `launching content`() {
        runBlocking {


            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.ShowFirstText("text"))
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
    fun `checking solution`() {
        runBlocking {

            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            mainViewModel.checkSolution("")


            verify(observer).onChanged(MainViewModel.UiModel.ShowEmptyInput)
        }
    }

    @Test
    fun `checking ok solution`() {
        runBlocking {


            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            mainViewModel.checkSolution("text")

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.ShowFirstText("text"))
            verify(observer).onChanged(MainViewModel.UiModel.EnableButton)
        }
    }

    @Test
    fun `checking wrong solution`() {
        runBlocking {

            mainViewModel.model.observeForever(observer)
            mainViewModel.myLaunch()
            mainViewModel.checkSolution("tet")

            verify(observer).onChanged(MainViewModel.UiModel.RequestCheckInternet)
            verify(observer).onChanged(MainViewModel.UiModel.ShowFirstText("text"))
            verify(observer).onChanged(MainViewModel.UiModel.DisableButton)
        }
    }

}