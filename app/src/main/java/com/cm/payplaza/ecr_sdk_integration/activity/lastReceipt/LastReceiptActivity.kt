package com.cm.payplaza.ecr_sdk_integration.activity.lastReceipt

import android.view.LayoutInflater
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.EcrRouter
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ActivityLastReceiptBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LastReceiptActivity: BaseEcrFragmentActivity<
        LastReceiptViewModel,
        ActivityLastReceiptBinding>() {
    override val viewModel: LastReceiptViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            LastReceiptState.OnCrash -> goToErrorFragment()
            LastReceiptState.OnError -> goToErrorFragment()
            LastReceiptState.OnResult -> goToReceiptFragment()
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
        return binding.lastReceiptViewFragment.findNavController()
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityLastReceiptBinding {
        Timber.d("getActivityNavController")
        return ActivityLastReceiptBinding.inflate(layoutInflater)
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("getActivityNavController")
        binding.lastReceiptDrawer.openDrawer(binding.lastReceiptNavigation)
        return true
    }

    override fun onBackPressed() {
        if (binding.lastReceiptDrawer.isDrawerOpen(GravityCompat.START)) {
            Timber.d("getActivityNavController")
            binding.lastReceiptDrawer.closeDrawer(GravityCompat.START)
        } else {
            Timber.d("getActivityNavController")
            super.onBackPressed()
        }
    }

    override fun setUpMenu() {
        binding.lastReceiptNavigation.setNavigationItemSelectedListener { menuItem ->
            binding.lastReceiptDrawer.closeDrawer(GravityCompat.START)
            when(menuItem.itemId) {
                R.id.nav_day_totals -> {
                    EcrRouter.goToTotals(this)
                    true
                }
                R.id.nav_new_payment -> {
                    EcrRouter.goToPaymentActivity(this)
                    true
                }
                R.id.nav_refund -> {
                    EcrRouter.goToRefundActivity(this)
                    true
                }
                else -> false
            }
        }
        val menu = binding.lastReceiptNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_print_last_receipt).isEnabled = false
    }

    override fun initializeView(terminalData: TerminalData?) {
        Timber.d("initializeView")
        navController = getActivityNavController()
        setUpToolbar(binding.lastReceiptToolbar, binding.lastReceiptDrawer)
        setUpMenu()
        setUpBookmark()
        setUpVersions(terminalData)
        viewModel.getLastReceipt()
    }

    override fun setUpVersions(terminalData: TerminalData?) {
        terminalData?.let {
            binding.lastReceiptSwVersion.text = String.format(getString(R.string.software_version, terminalData.versionNumber))
            binding.lastReceiptDeviceSn.text = String.format(getString(R.string.device_serial_number, terminalData.deviceSerialNumber))
        }
    }

    private fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.paymentBookmarkBar.setSelectedBookmark(5, R.string.bookmark_receipt)
    }

    private fun goToReceiptFragment() {
        Timber.d("goToReceiptFragment")
        navController.navigate(R.id.action_loaderFragment2_to_receiptViewFragment2)
    }

    private fun goToErrorFragment() {
        Timber.d("goToErrorFragment")
        navController.navigate(R.id.action_loaderFragment2_to_errorFragment2)
    }
}