package com.a.getmimo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a.getmimo.data.source.networking.Status
import com.a.getmimo.domain.entity.Lesson
import com.a.getmimo.domain.usecases.GetLessons
import com.a.getmimo.ui.common.ScopedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(
    val getLessons: GetLessons,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    sealed class UiModel {

        object ShowCanCheckYourInternet : UiModel()
        object Loading : UiModel()
        object ShowErrorCall : UiModel()
        object ShowEmptyData : UiModel()
        data class Content(val lesson: List<Lesson>?) : UiModel()
        object RequestCheckInternet : UiModel()
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
                            _model.value = UiModel.Content(resource.data)
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
}