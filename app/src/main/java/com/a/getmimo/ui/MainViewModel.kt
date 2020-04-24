package com.a.getmimo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.getmimo.domain.entity.Content
import com.a.getmimo.domain.entity.Lesson
import com.a.getmimo.domain.entity.networking.Status
import com.a.getmimo.domain.usecases.GetLessons
import com.a.getmimo.ui.common.ScopedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(
    val getLessons: GetLessons,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    lateinit var solution: String
    lateinit var lessons: List<Lesson>
    var myIterator = 0

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    sealed class UiModel {

        object Done : UiModel()
        object Loading : UiModel()
        object ShowCanCheckYourInternet : UiModel()
        object ShowErrorCall : UiModel()
        object ShowEmptyData : UiModel()
        object RequestCheckInternet : UiModel()
        object EnableButton : UiModel()
        object DisableButton : UiModel()
        object ShowEmptyInput : UiModel()
        data class ShowFirstText(val firstText: String) : UiModel()
        data class ShowSecondText(val secondText: String) : UiModel()
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

    fun checkInput() {

        if (myIterator < lessons.size) {
            if (lessons[myIterator].startIndex != null &&
                lessons[myIterator].startIndex != null &&
                lessons[myIterator].content != null
            ) {

                if (lessons[myIterator].content!!.size > 1) {
                    for ((index, content) in lessons[myIterator].content!!.withIndex()) {
                        checkThreeContent(content, index)
                    }
                } else {
                    checkOneContent(
                        lessons[myIterator].content!![0].text!!,
                        lessons[myIterator].startIndex!!,
                        lessons[myIterator].endIndex!!,
                        lessons[myIterator].content!!.size
                    )
                }
            } else {
                myIterator += 1
                checkInput()
            }

        } else {
            _model.value = UiModel.Done
        }

    }

    fun checkThreeContent(content: Content, index: Int) {
        myIterator += 1
        when (index) {
            0 -> {
                content.text?.let {
                    _model.value = UiModel.ShowFirstText(content.text)
                }
            }
            1 -> {
                content.text?.let {
                    solution = content.text
                }
            }
            2 -> {
                content.text?.let {
                    _model.value = UiModel.ShowSecondText(content.text)
                }
            }
        }
    }

    fun checkOneContent(text: String, startIndex: Int, endIndex: Int, finish: Int) {
        myIterator += 1
        _model.value = UiModel.ShowFirstText(
            text.subSequence(
                0,
                startIndex
            ).toString()
        )

        solution = text.substring(
            startIndex,
            endIndex
        )

        _model.value = UiModel.ShowSecondText(
            text.subSequence(
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