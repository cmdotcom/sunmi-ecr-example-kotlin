package com.cm.payplaza.ecr_sdk_integration.activity.statuses

import android.view.LayoutInflater
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.EcrRouter
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ActivityStatusesBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.statuses.StatusesFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StatusesActivity: BaseEcrFragmentActivity<StatusesViewModel, ActivityStatusesBinding>() {
    override val viewModel: StatusesViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            StatusesState.OnResult -> goToStatusesFragment()
            StatusesState.OnCrash -> goToErrorFragment()
            StatusesState.OnError -> goToErrorFragment()
        }
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityStatusesBinding {
        Timber.d("getActivityNavController")
        return ActivityStatusesBinding.inflate(layoutInflater)
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            StatusesFragmentState.NoDataAvailable -> goToErrorFromStatuses()
            StatusesFragmentState.GoToReceiptView -> goToReceiptView()
            StatusesFragmentState.GoToLastReceipt -> EcrRouter.goToLastReceipt(this)
            StatusesFragmentState.GoToPayment -> EcrRouter.goToPaymentActivity(this)
            ErrorFragmentState.Dismiss -> EcrRouter.goToPaymentActivity(this)
            ReceiptViewFragmentState.FinishTransaction -> goToStatusesFromReceipt()
            ReceiptViewFragmentState.ControlledTransactionError -> goToStatusesFromReceipt()
            else -> { }
        }
    }

    override fun getActivityNavController(): NavController {
        Timber.d("getActivityNavController")
        return binding.statusesViewFragment.findNavController()
    }

    override fun setUpMenu() {
        binding.statusesNavigation.setNavigationItemSelectedListener {
            binding.statusesDrawer.closeDrawer(GravityCompat.START)
            false
        }
        val menu = binding.statusesNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_print_last_receipt).isEnabled = false
        menu.findItem(R.id.nav_day_totals).isEnabled = false
        menu.findItem(R.id.nav_new_payment).isEnabled = false
        menu.findItem(R.id.nav_refund).isEnabled = false
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("getActivityNavController")
        binding.statusesDrawer.openDrawer(binding.statusesNavigation)
        return true
    }

    override fun onBackPressed() {
        if (binding.statusesDrawer.isDrawerOpen(GravityCompat.START)) {
            Timber.d("getActivityNavController")
            binding.statusesDrawer.closeDrawer(GravityCompat.START)
        } else {
            Timber.d("getActivityNavController")
            super.onBackPressed()
        }
    }

    override fun initializeView(terminalData: TerminalData?) {
        Timber.d("initializeView")
        navController = getActivityNavController()
        setUpToolbar(binding.statusesToolbar, binding.statusesDrawer)
        setUpMenu()
        setUpBookmark()
        viewModel.getStatuses()
        setUpVersions(terminalData)
    }

    override fun setUpVersions(terminalData: TerminalData?) {
        terminalData?.let {
            binding.statusesSwVersion.text = String.format(getString(R.string.software_version, terminalData.versionNumber))
            binding.statusesDeviceSn.text = String.format(getString(R.string.device_serial_number, terminalData.deviceSerialNumber))
        }
    }

    private fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.statusesBookmarkBar.setSelectedBookmark(5, R.string.statuses)
    }

    private fun goToErrorFragment() {
        Timber.d("goToErrorFragment")
        navController.navigate(R.id.action_loaderFragment4_to_errorFragment3)
    }

    private fun goToErrorFromStatuses() {
        Timber.d("goToErrorFragment")
        navController.navigate(R.id.action_statusesFragment_to_errorFragment3)
    }

    private fun goToStatusesFragment() {
        Timber.d("goToStatusesFragment")
        navController.navigate(R.id.action_loaderFragment4_to_statusesFragment)
    }

    private fun goToReceiptView() {
        Timber.d("goToReceiptView")
        navController.navigate(R.id.action_statusesFragment_to_receiptViewFragment3)
    }

    private fun goToStatusesFromReceipt() {
        Timber.d("goToStatusesFromReceipt")
        navController.navigate(R.id.action_receiptViewFragment3_to_statusesFragment4)
    }
}