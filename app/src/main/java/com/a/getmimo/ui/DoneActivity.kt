package com.a.getmimo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.a.getmimo.R
import kotlinx.android.synthetic.main.done_dialog.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DoneActivity : AppCompatActivity() {
    private val viewModel: DoneViewModel by currentScope.viewModel(this) {
        parametersOf(intent.getIntExtra(ID_LESSON, 1), intent!!.getLongExtra(START_LESSON,0L))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.done_dialog)
        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun updateUi(model: DoneViewModel.UiModel) {
        when (model) {
            is DoneViewModel.UiModel.ShowFinishedIcon
            -> showFinishIcon()
        }
    }

    private fun showFinishIcon() {
        done_image.visibility = VISIBLE
    }

    companion object {

        const val ID_LESSON = "idLesson"
        const val START_LESSON = "startLesson"
    }

}