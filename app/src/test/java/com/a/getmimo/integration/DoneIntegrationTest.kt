package com.a.getmimo.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.a.getmimo.FakeLocalDataSource
import com.a.getmimo.data.source.local.LocalDataSource
import com.a.getmimo.domain.entity.SimpleLesson
import com.a.getmimo.idLesson
import com.a.getmimo.initMockedDi
import com.a.getmimo.startDate
import com.a.getmimo.ui.DoneViewModel
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class DoneIntegrationTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<DoneViewModel.UiModel>

    lateinit var doneViewModel: DoneViewModel

    private lateinit var localDataSource: FakeLocalDataSource

    private lateinit var calendar: Calendar
        @Before
    fun setup() {
        calendar = Calendar.getInstance()
        val viewModelModule = module {
            factory { (idLesson: Int, startDate: Long) ->
                DoneViewModel(
                    idLesson,
                    startDate,
                    get(),
                    get()
                )
            }
        }
        initMockedDi(viewModelModule)

        doneViewModel = get { parametersOf(idLesson, startDate) }

        localDataSource = get<LocalDataSource>() as FakeLocalDataSource
    }

    @Test
    fun `saving Lesson`() {
        val simpleLessonMocked = SimpleLesson(startDate, calendar.timeInMillis, idLesson)

        doneViewModel.model.observeForever(observer)
        doneViewModel.saveTheLesson(calendar)

        verify(observer).onChanged(DoneViewModel.UiModel.ShowFinishedIcon(simpleLessonMocked))
    }


}