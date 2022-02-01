package com.cm.payplaza.ecr_sdk_integration.activity.refund

import android.view.LayoutInflater
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.EcrRouter
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ActivityRefundBinding
import com.cm.payplaza.ecr_sdk_integration.dialog.BaseEcrDialog
import com.cm.payplaza.ecr_sdk_integration.dialog.CancelDialog
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert.DateInsertFramentState
import com.cm.payplaza.ecr_sdk_integration.fragment.passwordInsert.PasswordInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert.StanInsertState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class RefundActivity: BaseEcrFragmentActivity<
        RefundViewModel,
        ActivityRefundBinding>(),
    BaseEcrDialog.ActionListener {
    override val viewModel: RefundViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            RefundViewState.GoToTransactionActivity -> EcrRouter.goToTransactionResultActivity(this)
            RefundViewState.GoToStanInsert -> goToStanInsertFragment()
            RefundViewState.SkipStanInsert -> viewModel.prepareRefund()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            is AmountInsertFragmentState.NextStep -> {
                viewModel.saveAmount(state.amount)
                viewModel.checkForStanInsert()
            }
            PasswordInsertFragmentState.OkPin -> goToAmountInsertFragment()
            is StanInsertState.SaveStand -> saveStand(state.stand)
            is DateInsertFramentState.ConfirmDate -> {
                viewModel.saveDate(state.date)
                viewModel.prepareRefund()
            }
        }
    }

    override fun getActivityNavController(): NavController {
        Timber.d("getActivityNavController")
        return binding.refundFragment.findNavController()
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("onSupportNavigateUp")
        binding.refundDrawer.openDrawer(binding.refundNavigation)
        return true
    }

    override fun onBackPressed() {
        if (binding.refundDrawer.isDrawerOpen(GravityCompat.START)) {
            Timber.d("isDrawerOpen")
            binding.refundDrawer.closeDrawer(GravityCompat.START)
        } else {
            Timber.d("onBackPressed")
            super.onBackPressed()
        }
    }

    override fun getViewBinding(layoutInflater: LayoutInflater): ActivityRefundBinding {
        Timber.d("getViewBinding")
        return ActivityRefundBinding.inflate(layoutInflater)
    }

    override fun setUpMenu() {
        binding.refundNavigation.setNavigationItemSelectedListener { menuItem ->
            binding.refundDrawer.closeDrawer(GravityCompat.START)
            when(menuItem.itemId) {
                R.id.nav_day_totals -> {
                    EcrRouter.goToTotals(this)
                    true
                }
                R.id.nav_print_last_receipt -> {
                    EcrRouter.goToLastReceipt(this)
                    true
                }
                R.id.nav_new_payment -> {
                    EcrRouter.goToPaymentActivity(this)
                    true
                }
                R.id.nav_cancel_payment -> {
                    showCancelDialog()
                    true
                }
                R.id.nav_request_info -> {
                    requestInfo()
                    true
                }
                else -> false
            }
        }
        val menu = binding.refundNavigation.menu
        menu.findItem(R.id.nav_refund).isEnabled = false
    }

    private fun setUpBookmark() {
        binding.refundBookmarkBar.setSelectedBookmark(1, R.string.refund)
    }

    override fun initializeView(terminalData: TerminalData?) {
        Timber.d("initializeView")
        navController = getActivityNavController()
        setUpToolbar(binding.refundToolbar, binding.refundDrawer)
        setUpMenu()
        setUpBookmark()
        setUpVersions(terminalData)
    }

    override fun setUpVersions(terminalData: TerminalData?) {
        terminalData?.let {
            binding.refundSwVersion.text = String.format(getString(R.string.software_version, terminalData.versionNumber))
            binding.refundDeviceSn.text = String.format(getString(R.string.device_serial_number, terminalData.deviceSerialNumber))
        }
    }

    private fun saveStand(stand: String) {
        Timber.d("saveStand")
        viewModel.saveStan(stand)
        goToDateInsertFragment()
    }

    private fun goToStanInsertFragment() {
        Timber.d("goToStandInsertFragment")
        binding.refundBookmarkBar.setSelectedBookmark(1, R.string.refund)
        navController.navigate(R.id.action_amountInsertFragment2_to_stanInsertFragment)
    }

    private fun goToAmountInsertFragment() {
        binding.refundBookmarkBar.setSelectedBookmark(1, R.string.refund)
        Timber.d("goToAmountInsertFragment")
        navController.navigate(R.id.action_passwordInsertFragment_to_amountInsertFragment2)
    }

    private fun goToDateInsertFragment() {
        Timber.d("goToDateInsert")
        binding.refundBookmarkBar.setSelectedBookmark(1, R.string.bookmark_date)
        navController.navigate(R.id.action_stanInsertFragment_to_dateInsertFragment)
    }

    private fun showCancelDialog() {
        Timber.d("showCancelDialog")
        CancelDialog(this).show(this.supportFragmentManager,"")
    }

    override fun onOkPressed() {
        Timber.d("onOkPressed")
        EcrRouter.goToPaymentActivity(this)
    }

    override fun onCancelPressed() = Timber.d("onCancelPressed")
}