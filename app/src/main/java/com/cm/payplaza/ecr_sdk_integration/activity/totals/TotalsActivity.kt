package com.cm.payplaza.ecr_sdk_integration.activity.totals

import android.view.LayoutInflater
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.EcrRouter
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ActivityTotalsBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TotalsActivity: BaseEcrFragmentActivity<TotalsViewModel, ActivityTotalsBinding>() {
    override val viewModel: TotalsViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            TotalsState.OnCrash -> goToErrorFragment()
            TotalsState.OnError -> goToErrorFragment()
            TotalsState.OnResult -> goToReceiptFragment()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            ErrorFragmentState.Dismiss -> EcrRouter.goToPaymentActivity(this)
            is ReceiptViewFragmentState.ControlledTransactionError -> goToErrorFragment()
            ReceiptViewFragmentState.FinishTransaction -> EcrRouter.goToPaymentActivity(this)
        }
    }

    override fun getActivityNavController(): NavController {
        Timber.d("getActivityNavController")
        return binding.totalsFragment.findNavController()
    }

    override fun setUpMenu() {
        binding.totalsNavigation.setNavigationItemSelectedListener { menuItem ->
            binding.totalsDrawer.closeDrawer(GravityCompat.START)
            when(menuItem.itemId) {
                R.id.nav_print_last_receipt -> {
                    EcrRouter.goToLastReceipt(this)
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
                R.id.nav_request_info -> {
                    requestInfo()
                    true
                }
                else -> false
            }
        }
        val menu = binding.totalsNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_day_totals).isEnabled = false
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityTotalsBinding {
        Timber.d("getViewBinding")
        return ActivityTotalsBinding.inflate(layoutInflater)
    }

    override fun initializeView(terminalData: TerminalData?) {
        Timber.d("initializeView")
        setUpBookmark()
        setUpToolbar(binding.totalsToolbar, binding.totalsDrawer)
        setUpMenu()
        setUpVersions(terminalData)
        navController = getActivityNavController()
        viewModel.getTotals()
    }

    override fun setUpVersions(terminalData: TerminalData?) {
        terminalData?.let {
            binding.totalsSwVersion.text = String.format(getString(R.string.software_version, terminalData.versionNumber))
            binding.totalsDeviceSn.text = String.format(getString(R.string.device_serial_number, terminalData.deviceSerialNumber))
        }
    }

    private fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.totalsBookmarkBar.setSelectedBookmark(5, R.string.bookmark_totals)
    }

    private fun goToReceiptFragment() {
        Timber.d("goToReceiptFragment")
        navController.navigate(R.id.action_loaderFragment2_to_receiptViewFragment2)
    }

    private fun goToErrorFragment() {
        Timber.d("goToErrorFragment")
        navController.navigate(R.id.action_loaderFragment2_to_receiptViewFragment2)
    }
}