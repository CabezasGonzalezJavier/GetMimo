package com.a.getmimo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.a.getmimo.domain.entity.SimpleLesson
import com.a.getmimo.domain.usecases.SaveLesson
import com.a.getmimo.idLesson
import com.a.getmimo.simpleLessonMocked
import com.a.getmimo.startDate
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
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class DoneViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var saveLesson: SaveLesson

    @Mock
    lateinit var observer: Observer<DoneViewModel.UiModel>

    private lateinit var calendar: Calendar

    lateinit var doneViewModel: DoneViewModel

    @Before
    fun setup() {
        calendar = Calendar.getInstance()
        doneViewModel = DoneViewModel(
            idLesson,
            startDate,
            saveLesson,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `saving lesson`() {
        runBlocking {

            val simpleLessonMocked = SimpleLesson(startDate, calendar.timeInMillis, idLesson)

            whenever(saveLesson.invoke(simpleLessonMocked)).thenReturn(simpleLessonMocked)
            doneViewModel.model.observeForever(observer)
            doneViewModel.saveTheLesson(calendar)
            val lesson = saveLesson.invoke(simpleLessonMocked)

            verify(observer).onChanged(DoneViewModel.UiModel.ShowFinishedIcon(simpleLessonMocked))
            assertEquals(lesson, simpleLessonMocked)
        }
    }
}