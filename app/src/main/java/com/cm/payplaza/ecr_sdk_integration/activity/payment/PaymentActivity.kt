package com.cm.payplaza.ecr_sdk_integration.activity.payment

import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.EcrRouter
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ActivityPaymentBinding
import com.cm.payplaza.ecr_sdk_integration.dialog.BaseEcrDialog
import com.cm.payplaza.ecr_sdk_integration.dialog.EnableAutoTimezoneDialog
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PaymentActivity: BaseEcrFragmentActivity<
        PaymentViewModel,
        ActivityPaymentBinding>() {
    private var dataLoaded = false
    override val viewModel: PaymentViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        when(state) {
            is BaseEcrViewState.RequestInfo -> {
                if(dataLoaded) { setUpVersions(state.terminalData) }
                else { setUpTerminalData(state.terminalData) }
                checkForAutoTimezone()
            }
            PaymentViewState.EnableAutoTimezone -> askForEnableAutoTimezone()
            PaymentViewState.GoToTransactionResult -> EcrRouter.goToTransactionResultActivity(this)
        }
        super.render(state)
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            is AmountInsertFragmentState.NextStep -> savePayment(state.amount)
            ErrorFragmentState.Dismiss -> supportFragmentManager.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("onSupportNavigateUp")
        binding.paymentAmountInsertDrawer.openDrawer(binding.paymentNavigation)
        return true
    }

    override fun onBackPressed() {
        if (binding.paymentAmountInsertDrawer.isDrawerOpen(GravityCompat.START)) {
            Timber.d("onBackPressed - closeDrawer")
            binding.paymentAmountInsertDrawer.closeDrawer(GravityCompat.START)
        } else {
            Timber.d("onBackPressed")
            super.onBackPressed()
        }
    }

    override fun setUpMenu() {
        binding.paymentNavigation.setNavigationItemSelectedListener { menuItem ->
            binding.paymentAmountInsertDrawer.closeDrawer(GravityCompat.START)
            when(menuItem.itemId) {
                R.id.nav_day_totals -> {
                    EcrRouter.goToTotals(this)
                    true
                }
                R.id.nav_print_last_receipt -> {
                    EcrRouter.goToLastReceipt(this)
                    true
                }
                R.id.nav_refund -> {
                    EcrRouter.goToRefundActivity(this)
                    true
                }
                R.id.nav_request_info -> {
                    requestInfo()
                    true
                }
                else -> false
            }
        }
        val menu = binding.paymentNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_new_payment).isEnabled = false
    }

    override fun initializeView(terminalData: TerminalData?) {
        Timber.d("initializeView")
        navController = getActivityNavController()
        setUpToolbar(binding.amountInsertToolbar, binding.paymentAmountInsertDrawer)
        setUpMenu()
        setUpBookmark()
        requestInfo()
    }

    private fun savePayment(amount: Int) {
        viewModel.savePayment(amount)
    }

    private fun setUpTerminalData(terminalData: TerminalData?) {
        setUpVersions(terminalData)
        goToAmountInsertFragment()
        dataLoaded = true
    }

    override fun setUpVersions(terminalData: TerminalData?) {
        terminalData?.let{
            binding.paymentSwVersion.text = String.format(getString(R.string.software_version, terminalData.versionNumber))
            binding.paymentDeviceSn.text = String.format(getString(R.string.device_serial_number, terminalData.deviceSerialNumber))
        }
    }

    override fun getActivityNavController(): NavController {
        Timber.d("getActivityNavController")
        return binding.receiptViewFragment.findNavController()
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityPaymentBinding {
        Timber.d("getViewBinding")
        return ActivityPaymentBinding.inflate(layoutInflater)
    }

    private fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.paymentBookmarkBar.setSelectedBookmark(1, R.string.payment)
    }

    private fun goToAmountInsertFragment() {
        Timber.d("goToAmountInsertFragment")
        navController.navigate(R.id.action_loaderFragment3_to_amountInsertFragment)
    }

    private fun askForEnableAutoTimezone() {
        val currentContext = applicationContext
        val listener = object : BaseEcrDialog.ActionListener {
            override fun onOkPressed() = goToDataAndTimeSettings()
            override fun onCancelPressed() {
                Toast.makeText(currentContext, R.string.enable_button, Toast.LENGTH_LONG).show()
                finish()
            }
        }
        EnableAutoTimezoneDialog(listener).show(this.supportFragmentManager,"")
    }

    private fun goToDataAndTimeSettings() {
        val intent = Intent(Settings.ACTION_DATE_SETTINGS)
        startActivity(intent)
    }

    private fun checkForAutoTimezone() {
        viewModel.checkAutoTimezone()
    }
}