package com.cm.payplaza.ecr_sdk_integration.activity.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.LocaleListCompat
import androidx.core.view.GravityCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.cm.payplaza.ecr_sdk_integration.BuildConfig
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt.LastReceiptActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish.FinishPreauthActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.MenuItem
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.MenuItemsAdapter
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.MenuItemsDataHolder
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthType
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.start.PreAuthActivity
import com.cm.payplaza.ecr_sdk_integration.activity.refund.RefundActivity
import com.cm.payplaza.ecr_sdk_integration.activity.totals.TotalsActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ActivityEcrBinding
import com.cm.payplaza.ecr_sdk_integration.dialog.*
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.utils.LocaleHelper
import timber.log.Timber

abstract class BaseEcrActivity<VM : BaseEcrViewModel> : AppCompatActivity() {

    protected lateinit var binding: ActivityEcrBinding
    private var dialog: AlertDialog? = null
    private var menuItemsAdapter: MenuItemsAdapter? = null
    protected var isActivityRestored: Boolean = false
    protected var downloadingParameters: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEcrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isActivityRestored = savedInstanceState != null
        viewModel.state.observe(this) {
            render(it)
        }
        viewModel.loadTerminalData()
    }

    override fun onStart() {
        super.onStart()
        binding.bottomAppView.setTransactionTypeText(getTransactionTypeStringId())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavigationBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("onSupportNavigateUp")
        binding.drawer.openDrawer(binding.navigationLayout.navView)
        return true
    }

    fun hideNavigationBar() {
        Timber.d("hideNavigationBar()")
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    protected open fun render(state: BaseEcrViewState) {
        when (state) {
            is BaseEcrViewState.Init -> {
                showTerminalData(state.terminalData)
                updateLanguage(state.terminalData)
            }
            is BaseEcrViewState.RequestInfo -> {
                dismissDialog()
                setUpVersions(state.terminalData)
                updateLanguageAndRestartActivity(state.terminalData)
                downloadingParameters = false
            }
            is BaseEcrViewState.RequestInfoFailed -> showRequestInfoFailedDialog()
        }
    }

    private fun dismissDialog() {
        dialog?.dismiss()
    }

    private fun updateLanguage(data: TerminalData?) {
        data?.let {
            val newLanguage = it.storeLanguage ?: LocaleHelper.DEFAULT_LANGUAGE
            val locale = it.storeCountry ?: LocaleHelper.DEFAULT_COUNTRY_CODE
            LocaleHelper.setLocale(applicationContext, newLanguage, locale)

            // Update app compat Delegate so in newer versions of Android is also updating language
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(newLanguage)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }

    private fun updateLanguageAndRestartActivity(data: TerminalData?) {
        data?.let {
            val newLanguage = it.storeLanguage ?: LocaleHelper.DEFAULT_LANGUAGE
            val previousLanguage = LocaleHelper.getPersistedData(applicationContext)
            val countryCode = it.storeCountry ?: LocaleHelper.DEFAULT_COUNTRY_CODE
            LocaleHelper.setLocale(applicationContext, newLanguage, countryCode)

            // Update app compat Delegate so in newer versions of Android is also updating language
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(newLanguage)
            AppCompatDelegate.setApplicationLocales(appLocale)

            if (previousLanguage != newLanguage) {
                finish()
                val intent = intent.apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }
    }

    private fun showRequestInfoFailedDialog() {
        dismissDialog()
        val listener = object : DialogLauncher.ActionListener {
            override fun onCancelPressed() {
                finish()
            }

            override fun onDismiss() {
                hideNavigationBar()
            }

            override fun onOkPressed() {
                viewModel.requestInfo()
            }
        }
        DialogLauncher(this).showAlertDialog(
            listener,
            titleStringId = R.string.request_info_dialog,
            hasNegativeButton = true,
            hasPositiveButton = true,
            cancellable = false
        )
    }

    open fun setUpMenu() {
        binding.navigationLayout.navView.setNavigationItemSelectedListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            true
        }

        setMenuStatuses(listOf(Pair(getString(R.string.cancel_payment), false)))

        with(binding.navigationLayout.ecrPreauthExpandibleList) {
            menuItemsAdapter = MenuItemsDataHolder.getListAdapter(context, PreauthType.NONE)

            setOnGroupClickListener { parent, itemView, groupPosition, groupId ->
                menuGroupItemClickListener(parent, itemView, groupPosition, groupId)
            }

            setOnChildClickListener { parent, itemView, groupPosition, childPosition, childId ->
                menuChildItemClickHandler(
                    parent,
                    itemView,
                    groupPosition,
                    childPosition,
                    childId,
                    PreauthType.NONE
                )
            }

            setAdapter(menuItemsAdapter)
            visibility = View.VISIBLE
        }
    }

    @Suppress("UNUSED_PARAMETER")
    protected fun menuGroupItemClickListener(
        parent: ExpandableListView,
        itemView: View,
        groupPosition: Int,
        groupId: Long
    ): Boolean {

        if (currentItemSelected == groupPosition && currentItemSelected != MenuItem.PRE_AUTH.value) {
            return true
        }
        when (groupPosition) {
            MenuItem.NEW_PAYMENT.value -> {
                binding.drawer.closeDrawer(GravityCompat.START)
                PaymentActivity.start(this)
            }
            MenuItem.PRE_AUTH.value -> {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition)
                } else {
                    parent.expandGroup(groupPosition)
                }
            }
            MenuItem.REFUND.value -> {
                binding.drawer.closeDrawer(GravityCompat.START)
                RefundActivity.start(this)
            }
            MenuItem.PRINT_LAST_RECEIPT.value -> {
                binding.drawer.closeDrawer(GravityCompat.START)
                LastReceiptActivity.start(this)
            }
            MenuItem.DAY_TOTALS.value -> {
                binding.drawer.closeDrawer(GravityCompat.START)
                TotalsActivity.start(this)
            }
            MenuItem.DOWNLOAD_PARAMETERS.value -> {
                downloadingParameters = true
                binding.drawer.closeDrawer(GravityCompat.START)
                viewModel.requestInfo()
            }
            MenuItem.CANCEL_PAYMENT.value -> {
                val isEnabled = menuItemsAdapter?.isMenuItemEnabled(MenuItem.CANCEL_PAYMENT)

                if (isEnabled == true && (currentItemSelected == MenuItem.REFUND.value || currentItemSelected == MenuItem.PRE_AUTH.value)) {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    showCancelDialog()
                }
            }
        }

        if (groupPosition != MenuItem.CANCEL_PAYMENT.value &&
            groupPosition != MenuItem.DOWNLOAD_PARAMETERS.value &&
            groupPosition != MenuItem.PRE_AUTH.value
        ) {
            // These two menu items do not represent a payment flow UI state.
            // So, do not include them in current selected positions.
            currentItemSelected = groupPosition
        }
        return true
    }

    @Suppress("UNUSED_PARAMETER")
    protected fun menuChildItemClickHandler(
        parent: ExpandableListView,
        itemView: View,
        groupPosition: Int,
        childPosition: Int,
        childId: Long,
        preAuthType: PreauthType
    ): Boolean {
        currentItemSelected = MenuItem.PRE_AUTH.value
        when (childPosition) {
            PreauthType.START.i -> {
                if (preAuthType != PreauthType.START) {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    PreAuthActivity.start(this)
                }
            }
            PreauthType.CONFIRM.i -> {
                if (preAuthType != PreauthType.CONFIRM) {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    FinishPreauthActivity.start(this, PreauthType.CONFIRM)
                }
            }
            PreauthType.CANCEL.i -> {
                if (preAuthType != PreauthType.CANCEL) {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    FinishPreauthActivity.start(this, PreauthType.CANCEL)
                }
            }
        }
        return true
    }

    protected fun setDefaultMenuItemSelected() {
        currentItemSelected = MenuItem.NEW_PAYMENT.value
    }

    fun setMenuStatuses(items: List<Pair<String, Boolean>>) {
        menuItemsAdapter?.setMenuItemStatuses(items)
    }

    protected open fun setUpVersions(terminalData: TerminalData?) {
        val navView = binding.navigationLayout.navView
        val header = navView.getHeaderView(0)
        val deviceSerial =
            header.findViewById<AppCompatTextView>(R.id.nav_header_device_serial_nr)
        val configurationVersion =
            header.findViewById<AppCompatTextView>(R.id.nav_header_device_configuration)
        terminalData?.let {
            val appVersion = BuildConfig.VERSION_NAME
            deviceSerial.text = String.format(
                getString(
                    R.string.device_serial_number,
                    terminalData.deviceSerialNumber
                )
            )
            configurationVersion.text =
                String.format(getString(R.string.software_version, appVersion))
        }
    }

    protected open fun showTerminalData(terminalData: TerminalData?) {
        Timber.d("showTerminalData")
        binding.toolbar.buttonHamburger.setOnClickListener {
            binding.drawer.openDrawer(
                GravityCompat.START
            )
        }
        setUpMenu()
        setUpVersions(terminalData)
    }

    private fun showCancelDialog() {
        Timber.d("showCancelDialog")
        val currentContext = this.applicationContext
        val listener = object : DialogLauncher.ActionListener {
            override fun onOkPressed() {
                Timber.d("onOkPressed")
                PaymentActivity.start(currentContext)
            }

            override fun onCancelPressed() = Timber.d("onCancelPressed")
            override fun onDismiss() {
                hideNavigationBar()
            }
        }
        DialogLauncher(this).showAlertDialog(
            listener,
            hasNegativeButton = true,
            titleStringId = R.string.cancel_operation_dialog
        )
    }

    protected abstract val viewModel: VM
    protected open fun restoreActivity() {}
    protected abstract fun getTransactionTypeStringId(): Int

    companion object {
        private var currentItemSelected = MenuItem.NEW_PAYMENT.value
    }
}