package com.cm.payplaza.ecr_sdk_integration.activity.transactionResult

import android.view.LayoutInflater
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.EcrRouter
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ActivityTransactionResultBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TransactionResultActivity: BaseEcrFragmentActivity<
        TransactionResultViewModel,
        ActivityTransactionResultBinding>() {
    override val viewModel: TransactionResultViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            TransactionResultState.GoToReceiptFragment -> goToReceiptFragment()
            TransactionResultState.OnResult -> goToReceiptFragment()
            TransactionResultState.OnFinishTransaction -> EcrRouter.goToPaymentActivity(this)
            TransactionResultState.OnCrash -> goToErrorFragment()
            TransactionResultState.OnError -> goToErrorFragment()
            TransactionResultState.GoToStatuses -> EcrRouter.goToStatusesActivity(this)
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            ErrorFragmentState.Dismiss -> EcrRouter.goToPaymentActivity(this)
            ReceiptViewFragmentState.ControlledTransactionError -> goToErrorFragment()
            ReceiptViewFragmentState.FinishTransaction -> EcrRouter.goToPaymentActivity(this)
        }
    }

    override fun getActivityNavController(): NavController {
        Timber.d("getActivityNavController")
        return binding.transactionViewFragment.findNavController()
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("onSupportNavigateUp")
        binding.transactionResultDrawer.openDrawer(binding.transactionNavigation)
        return true
    }

    override fun onBackPressed() {
        if (binding.transactionResultDrawer.isDrawerOpen(GravityCompat.START)) {
            Timber.d("onBackPressed - isDrawerOpen")
            binding.transactionResultDrawer.closeDrawer(GravityCompat.START)
        } else {
            Timber.d("onBackPressed")
            super.onBackPressed()
        }
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityTransactionResultBinding {
        Timber.d("getViewBinding")
        return ActivityTransactionResultBinding.inflate(layoutInflater)
    }

    override fun setUpMenu() {
        binding.transactionNavigation.setNavigationItemSelectedListener { menuItem ->
            binding.transactionResultDrawer.closeDrawer(GravityCompat.START)
            when(menuItem.itemId) {
                R.id.nav_new_payment -> {
                    EcrRouter.goToPaymentActivity(this)
                    true
                }
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
                R.id.nav_cancel_payment -> {
                    EcrRouter.goToPaymentActivity(this)
                    true
                }
                R.id.nav_request_info -> {
                    requestInfo()
                    true
                }
                else -> false
            }
        }
    }

    override fun initializeView(terminalData: TerminalData?) {
        Timber.d("initializeView")
        navController = getActivityNavController()
        setUpToolbar(binding.transactionViewToolbar, binding.transactionResultDrawer)
        setUpMenu()
        setUpVersions(terminalData)
        viewModel.doTransaction()
    }

    override fun setUpVersions(terminalData: TerminalData?) {
        terminalData?.let {
            binding.transactionSwVersion.text = String.format(getString(R.string.software_version, terminalData.versionNumber))
            binding.transactionDeviceSn.text = String.format(getString(R.string.device_serial_number, terminalData.deviceSerialNumber))
        }
    }

    private fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.transactionBookmarkBar.setSelectedBookmark(5, R.string.settings_receipts)
    }

    private fun goToReceiptFragment() {
        Timber.d("goToReceiptFragment")
        setUpBookmark()
        navController.navigate(R.id.action_loaderFragment_to_receiptViewFragment)
    }

    private fun goToErrorFragment() {
        Timber.d("goToErrorFragment")
        setUpBookmark()
        navController.navigate(R.id.action_loaderFragment_to_errorFragment)
    }
}