package com.cm.payplaza.ecr_sdk_integration.activity.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.test.espresso.idling.CountingIdlingResource
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt.LastReceiptActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthExpandibleListData
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthType
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.start.PreAuthActivity
import com.cm.payplaza.ecr_sdk_integration.activity.refund.RefundActivity
import com.cm.payplaza.ecr_sdk_integration.activity.totals.TotalsActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ActivityEcrBinding
import com.cm.payplaza.ecr_sdk_integration.dialog.BaseEcrDialog
import com.cm.payplaza.ecr_sdk_integration.dialog.CancelDialog
import com.cm.payplaza.ecr_sdk_integration.dialog.RequestInfoFailedDialog
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.utils.LocaleHelper
import timber.log.Timber

abstract class BaseEcrActivity<
        VM: BaseEcrViewModel> : AppCompatActivity() {
    protected lateinit var binding: ActivityEcrBinding
    private var dialog: AlertDialog? = null
    val mIdlingRes = CountingIdlingResource(BaseEcrActivity<*>::javaClass.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEcrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        init()
        Timber.d("Activity created")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    protected open fun init() {
        viewModel.state.observe(this) {
            render(it)
        }
        mIdlingRes.increment()
        viewModel.init()
    }

    private fun hideNavigationBar() {
        val decorView: View  = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("getActivityNavController")
        binding.ecrDrawer.openDrawer(binding.ecrNavigation)
        return true
    }

    protected open fun render(state: BaseEcrViewState) {
        when(state) {
            is BaseEcrViewState.Init -> {
                mIdlingRes.decrement()
                initializeView(state.terminalData)
                updateLanguage(state.terminalData)
            }
            is BaseEcrViewState.RequestInfo -> {
                dismissdialog()
                mIdlingRes.decrement()
                setUpVersions(state.terminalData)
                updateLanguage(state.terminalData)
            }
            is BaseEcrViewState.RequestInfoLoader -> showRequestDataLoader()
            is BaseEcrViewState.RequestInfoFailed -> showRequestInfoFailedDialog()
        }
    }

    private fun showRequestDataLoader() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        dialogBuilder.setView(inflater.inflate(R.layout.dialog_loader, null))
        dialogBuilder.setCancelable(true)
        dialog = dialogBuilder.create()
        dialog?.show()
    }

    private fun dismissdialog() {
        dialog?.dismiss()
    }

    private fun updateLanguage(data: TerminalData?) {
        data?.let {
            it.storeLanguage?.let { language ->
                LocaleHelper.setLocale(applicationContext, language)
            }
        }
    }

    private fun setUpToolbar(toolbar: Toolbar, drawer: DrawerLayout) {
        Timber.d("setUpToolbar")
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBarToggle = ActionBarDrawerToggle(this, drawer, 0, 0)
        drawer.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
    }

    protected fun requestInfo() {
        mIdlingRes.increment()
        viewModel.requestInfo()
    }

    private fun showRequestInfoFailedDialog() {
        dismissdialog()
        val listener = object : BaseEcrDialog.ActionListener {
            override fun onCancelPressed() = finish()
            override fun onDismiss() {}
            override fun onOkPressed() = viewModel.requestInfo()
        }
        RequestInfoFailedDialog(listener).show(this.supportFragmentManager,"")
    }

    protected open fun setUpMenu() {
        binding.ecrNavigation.setNavigationItemSelectedListener { menuItem ->
            binding.ecrDrawer.closeDrawer(GravityCompat.START)
            when(menuItem.itemId) {
                R.id.nav_day_totals -> {
                    TotalsActivity.start(this)
                    true
                }
                R.id.nav_print_last_receipt -> {
                    LastReceiptActivity.start(this)
                    true
                }
                R.id.nav_refund -> {
                    RefundActivity.start(this)
                    true
                }
                R.id.nav_request_info -> {
                    requestInfo()
                    true
                }
                R.id.nav_new_preauth -> {
                    PreAuthActivity.start(this)
                    true
                }
                R.id.nav_new_payment -> {
                    PaymentActivity.start(this)
                    true
                }
                R.id.nav_cancel_payment -> {
                    showCancelDialog()
                    true
                }
                else -> false
            }
        }
        binding.ecrNavigation.menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        binding.ecrPreauthExpandibleList.apply {
            val adapter = PreauthExpandibleListData.getPreauthListAdapter(context, PreauthType.NONE)
            setOnChildClickListener(adapter.getItemListener())
            setOnGroupClickListener(adapter.getGroupListener())
            setAdapter(adapter)
            visibility = View.VISIBLE
        }
    }

    protected open fun setUpVersions(terminalData: TerminalData?) {
        val navView = binding.ecrNavigation
        val header = navView.getHeaderView(0)
        val deviceSerial = header.findViewById<AppCompatTextView>(R.id.nav_header_device_serial_nr)
        val softwareVersion = header.findViewById<AppCompatTextView>(R.id.nav_header_sw_version)
        val configurationVersion = header.findViewById<AppCompatTextView>(R.id.nav_header_device_configuration)
        terminalData?.let {
            val appVersion = packageManager.getPackageInfo(packageName, 0).versionName
            softwareVersion.text = String.format(getString(R.string.payplaza_software_version, terminalData.versionNumber))
            deviceSerial.text = String.format(getString(R.string.device_serial_number, terminalData.deviceSerialNumber))
            configurationVersion.text = String.format(getString(R.string.software_version, appVersion))
        }
    }

    protected open fun initializeView(terminalData: TerminalData?) {
        Timber.d("initializeView")
        setUpToolbar(binding.ecrToolbar, binding.ecrDrawer)
        setUpMenu()
        setUpBookmark()
        setUpVersions(terminalData)
    }

    private fun showCancelDialog() {
        Timber.d("showCancelDialog")
        val currentContext = this.applicationContext
        val listener = object : BaseEcrDialog.ActionListener {
            override fun onOkPressed() {
                Timber.d("onOkPressed")
                PaymentActivity.start(currentContext)
            }
            override fun onCancelPressed() = Timber.d("onCancelPressed")
            override fun onDismiss() {}
        }
        CancelDialog(listener).show(this.supportFragmentManager,"")
    }

    protected abstract val viewModel: VM
    protected abstract fun setUpBookmark()
}