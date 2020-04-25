package com.a.getmimo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.getmimo.domain.entity.SimpleLesson
import com.a.getmimo.domain.usecases.SaveLesson
import com.a.getmimo.ui.common.ScopedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.*

class DoneViewModel(
    private val idLesson: Int,
    private val startLesson: Long,
    private val saveLesson: SaveLesson,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    private val calendar: Calendar = Calendar.getInstance()
    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) saveTheLesson(calendar)
            return _model
        }

    sealed class UiModel {
        data class ShowFinishedIcon(val simpleLesson: SimpleLesson) : UiModel()
    }


    init {
        initScope()
    }

    fun saveTheLesson(calendar: Calendar) = launch {
        _model.value = UiModel.ShowFinishedIcon(
            saveLesson.invoke(
                SimpleLesson(startLesson, calendar.timeInMillis, idLesson)
            )
        )
    }


}