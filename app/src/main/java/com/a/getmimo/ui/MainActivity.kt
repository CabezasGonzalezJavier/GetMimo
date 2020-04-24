package com.a.getmimo.ui

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.a.getmimo.R
import com.a.getmimo.ui.common.CheckInternet
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by currentScope.viewModel(this)


    private lateinit var checkInternet: CheckInternet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkInternet = CheckInternet(
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )

        viewModel.model.observe(this, Observer(::updateUi))
        main_button.setOnClickListener {
            if (main_editText.text.toString().isEmpty()) {
                viewModel.checkSolution(main_editText.text.toString())
            } else {

                viewModel.checkInput()
            }
        }

        main_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty()) {
                    showButtonEnable()
                } else {
                    viewModel.checkSolution(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun updateUi(model: MainViewModel.UiModel) {

        main_progressBar.visibility =
            if (model is MainViewModel.UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is MainViewModel.UiModel.Done -> showDone()
            is MainViewModel.UiModel.ShowEmptyData -> showEmptyData()
            is MainViewModel.UiModel.ShowErrorCall -> showErrorCall()
            is MainViewModel.UiModel.ShowCanCheckYourInternet -> showCanCheckYourInternet()
            is MainViewModel.UiModel.RequestCheckInternet -> checkInternet()
            is MainViewModel.UiModel.ShowFirstText -> showFirstText(model.firstText)
            is MainViewModel.UiModel.ShowSecondText -> showSecondText(model.secondText)
            is MainViewModel.UiModel.DisableButton -> showButtonDisable()
            is MainViewModel.UiModel.EnableButton -> showButtonEnable()
            is MainViewModel.UiModel.ShowEmptyInput -> showEmptyInput()
        }
    }

    private fun showDone() {
        DoneDialog.newInstance().show(supportFragmentManager, "")
    }

    private fun showEmptyInput() {
        val snackbar = Snackbar.make(
            main_content, getString(R.string.issue_empty_input),
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        snackbar.show()
    }

    private fun showButtonEnable() {
        main_button.isEnabled = true
        main_button.background = resources.getDrawable(R.drawable.background_button, null)
    }

    private fun showButtonDisable() {
        main_button.isEnabled = false
        main_button.background = resources.getDrawable(R.drawable.background_disable_button, null)
    }

    private fun showFirstText(firstText: String) {
        main_first.text = firstText
    }

    private fun showSecondText(secondText: String) {
        main_second.text = secondText
    }

    private fun checkInternet() {
        checkInternet.isOnline {
            viewModel.checkInternet(it)
        }
    }

    private fun showEmptyData() {
        val snackbar = Snackbar.make(
            main_content, getString(R.string.issue_empty_data),
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        snackbar.show()
    }

    private fun showErrorCall() {
        val snackbar = Snackbar.make(
            main_content, getString(R.string.issue_connection_error),
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        snackbar.show()
    }

    private fun showCanCheckYourInternet() {
        val snackbar = Snackbar.make(
            main_content, getString(R.string.issue_internet),
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        snackbar.show()
    }

}
