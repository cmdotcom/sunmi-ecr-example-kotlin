package com.cm.payplaza.ecr_sdk_integration.activity.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewbinding.ViewBinding
import com.cm.payplaza.ecr_sdk_integration.dialog.BaseEcrDialog
import com.cm.payplaza.ecr_sdk_integration.dialog.RequestInfoFailedDialog
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.utils.LocaleHelper
import timber.log.Timber

abstract class BaseEcrActivity<
        VM: BaseEcrViewModel,
        VB: ViewBinding> : AppCompatActivity(),
    BaseEcrDialog.ActionListener {
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding(layoutInflater)
        val view = binding.root
        setContentView(view)
        Timber.d("Activity created")
        init()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    protected open fun init() {
        viewModel.state.observe(this, {
            render(it)
        })
        viewModel.init()
    }

    private fun hideNavigationBar() {
        val decorView: View  = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    protected open fun render(state: BaseEcrViewState) {
        when(state) {
            is BaseEcrViewState.Init -> {
                initializeView(state.terminalData)
                updateLanguage(state.terminalData)
            }
            is BaseEcrViewState.RequestInfo -> {
                setUpVersions(state.terminalData)
                updateLanguage(state.terminalData)
            }
            is BaseEcrViewState.RequestInfoFailed -> showRequestInfoFailedDialog()
        }
    }

    private fun updateLanguage(data: TerminalData?) {
        data?.let {
            it.storeLanguage?.let { language ->
                LocaleHelper.setLocale(applicationContext, language)
            }
        }
    }

    protected fun setUpToolbar(toolbar: Toolbar, drawer: DrawerLayout) {
        Timber.d("setUpToolbar")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBarToggle = ActionBarDrawerToggle(this, drawer, 0, 0)
        drawer.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
    }

    override fun onCancelPressed() = finish()
    override fun onOkPressed() = viewModel.requestInfo()
    protected fun requestInfo() = viewModel.requestInfo()
    private fun showRequestInfoFailedDialog() = RequestInfoFailedDialog(this).show(this.supportFragmentManager,"")

    protected abstract val viewModel: VM
    protected abstract fun getViewBinding(layoutInflater: LayoutInflater): VB
    protected abstract fun setUpVersions(terminalData: TerminalData?)
    protected abstract fun initializeView(terminalData: TerminalData?)
    protected abstract fun setUpMenu()
}