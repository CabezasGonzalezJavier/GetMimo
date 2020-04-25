package com.a.getmimo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.getmimo.domain.entity.Lesson
import com.a.getmimo.domain.entity.networking.Status
import com.a.getmimo.domain.usecases.GetLessons
import com.a.getmimo.ui.common.Event
import com.a.getmimo.ui.common.ScopedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(
    private val getLessons: GetLessons,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    lateinit var lessons: List<Lesson>
    private var solution = ""
    var myIterator = 0

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }


    private val _navigation = MutableLiveData<Event<Lesson>>()
    val navigation: LiveData<Event<Lesson>> = _navigation


    sealed class UiModel {

        object Loading : UiModel()
        object ShowCanCheckYourInternet : UiModel()
        object ShowErrorCall : UiModel()
        object ShowEmptyData : UiModel()
        object RequestCheckInternet : UiModel()
        object EnableButton : UiModel()
        object DisableButton : UiModel()
        object ShowEmptyInput : UiModel()
        data class Done(val idLesson: Int, val startLesson: Long) : UiModel()
        data class ShowText(val firstText: String, val secondText: String) : UiModel()
    }

    init {
        initScope()
    }

    fun myLaunch() {
        launch {
            _model.value = UiModel.Loading
            val resource = getLessons.invoke()
            when (resource.status) {
                Status.SUCCESS -> {
                    if (resource.data != null) {
                        if (resource.data.isEmpty()) {
                            _model.value = UiModel.ShowEmptyData
                        } else {
                            lessons = resource.data
                            checkInput()
                        }
                    } else {
                        _model.value = UiModel.ShowErrorCall
                    }
                }
                else -> {
                    _model.value = UiModel.ShowErrorCall
                }
            }

        }
    }

    fun refresh() {
        _model.value = UiModel.RequestCheckInternet
    }

    fun checkInternet(continuation: Boolean) {
        if (continuation) {
            myLaunch()
        } else {
            _model.value = UiModel.ShowCanCheckYourInternet
        }
    }

    private fun checkInput() {

        if (myIterator < lessons.size) {
            if (lessons[myIterator].startIndex != null &&
                lessons[myIterator].startIndex != null &&
                lessons[myIterator].content != null
            ) {
                if (lessons[myIterator].id != null) {
                    lessons[myIterator].startDate = Calendar.getInstance().timeInMillis
                    if (lessons[myIterator].content!!.size > 1) {

                        _model.value = UiModel.ShowText(
                            lessons[myIterator].content!![0].text!!,
                            lessons[myIterator].content!![2].text!!
                        )
                        solution = lessons[myIterator].content!![1].text!!
                    } else {
                        checkOneContent(
                            lessons[myIterator].content!![0].text!!,
                            lessons[myIterator].startIndex!!,
                            lessons[myIterator].endIndex!!,
                            lessons[myIterator].content!![0].text!!.length
                        )
                    }
                }
            } else {
                myIterator += 1
                checkInput()
            }
        }

    }

    fun lessonDone() {
        _navigation.value = Event(lessons[myIterator])
    }

    private fun checkOneContent(text: String, startIndex: Int, endIndex: Int, finish: Int) {

        solution = text.substring(
            startIndex,
            endIndex
        )

        _model.value = UiModel.ShowText(
            text.subSequence(
                0,
                startIndex
            ).toString(), text.subSequence(
                endIndex,
                finish
            ).toString()
        )
    }

    fun checkSolution(userSolution: String) {

        if (userSolution.isNotEmpty()) {
            if (solution == userSolution) {
                _model.value = UiModel.EnableButton
            } else {
                _model.value = UiModel.DisableButton
            }
        } else {
            _model.value = UiModel.ShowEmptyInput
        }
    }
}