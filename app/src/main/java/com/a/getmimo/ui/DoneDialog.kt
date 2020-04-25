package com.a.getmimo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.a.getmimo.R
import kotlinx.android.synthetic.main.done_dialog.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DoneDialog : DialogFragment() {
    private val viewModel: DoneViewModel by currentScope.viewModel(this) {
        parametersOf(arguments!!.getInt(ID_LESSON), arguments!!.getLong(START_LESSON))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.done_dialog, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        fun newInstance(idLesson: Int, startLesson: Long): DoneDialog {
            val doneDialog = DoneDialog()
            val args = Bundle()

            args.putLong(START_LESSON, startLesson)
            args.putInt(ID_LESSON, idLesson)

            doneDialog.arguments = args
            return doneDialog
        }
    }

}