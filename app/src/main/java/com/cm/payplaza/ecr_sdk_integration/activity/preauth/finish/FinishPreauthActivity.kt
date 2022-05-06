package com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish

import android.content.Context
import android.content.Intent
import android.view.View
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.payment.PaymentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthExpandibleListData
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthType
import com.cm.payplaza.ecr_sdk_integration.activity.statuses.StatusesActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.dateInsert.DateInsertFramentState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.receiptView.ReceiptViewFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.stanInsert.StanInsertState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.util.*

class FinishPreauthActivity: BaseEcrFragmentActivity<FinishPreauthViewModel>(),
KoinComponent {

    companion object {
        private const val PREAUTH_TYPE_EXTRA = "PREAUTH_TYPE_EXTRA"
        fun start(context: Context, type: PreauthType) {
            Timber.d("goToFinishPreauthActivity")
            val intent = Intent(context, FinishPreauthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(PREAUTH_TYPE_EXTRA, type)
            context.startActivity(intent)
        }
    }

    override val viewModel: FinishPreauthViewModel by inject()

    override fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.bookmark_preauth)
    }

    override fun setUpMenu() {
        super.setUpMenu()
        val menu = binding.ecrNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = true
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        val preauthType =  intent.getSerializableExtra(PREAUTH_TYPE_EXTRA) as PreauthType
        viewModel.savePreauthType(preauthType)
        binding.ecrPreauthExpandibleList.apply {
            val adapter = PreauthExpandibleListData.getPreauthListAdapter(context, preauthType)
            setOnChildClickListener(adapter.getItemListener())
            setOnGroupClickListener(adapter.getGroupListener())
            setAdapter(adapter)
            visibility = View.VISIBLE
        }
        if(preauthType == PreauthType.CANCEL) {
            skipAmountInsert()
        }
    }

    override fun render(state: BaseEcrViewState) {
        super.render(state)
        when(state) {
            FinishPreauthViewState.OnResult -> goToReceipt()
            FinishPreauthViewState.OnCrash -> StatusesActivity.start(this)
            FinishPreauthViewState.OnError -> goToError()
        }
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            is AmountInsertFragmentState.NextStep -> goToStanInsert(state.amount)
            is StanInsertState.SaveStand -> goToDateInsert(state.stand)
            is DateInsertFramentState.ConfirmDate -> goToLoader(state.date)
            ErrorFragmentState.Dismiss -> PaymentActivity.start(this)
            ReceiptViewFragmentState.FinishTransaction -> PaymentActivity.start(this)
            else -> {}
        }
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.finish_preauth_graph
    }

    private fun goToStanInsert(amount: Int) {
        viewModel.saveAmount(amount)
        Timber.d("goToStanInsert")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.enter_stan)
        navController.navigate(R.id.action_amountInsertFragment4_to_stanInsertFragment2)
    }

    private fun goToDateInsert(stan: String) {
        viewModel.saveStan(stan)
        Timber.d("goToDateInsert")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.bookmark_date)
        navController.navigate(R.id.action_stanInsertFragment2_to_dateInsertFragment2)
    }

    private fun goToLoader(date: Date) {
        viewModel.saveDate(date)
        Timber.d("goToLoader")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.bookmark_card_transaction)
        navController.navigate(R.id.action_dateInsertFragment2_to_loaderFragment5)
        viewModel.doPreauth()
    }

    private fun goToReceipt() {
        Timber.d("goToReceipt")
        binding.ecrBookmarkBar.setSelectedBookmark(5, R.string.bookmark_receipt)
        navController.navigate(R.id.action_loaderFragment5_to_receiptViewFragment4)
    }

    private fun goToError() {
        Timber.d("goToError")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.bookmark_date)
        navController.navigate(R.id.action_loaderFragment5_to_errorFragment4)
    }

    private fun skipAmountInsert() {
        Timber.d("skipAmountInsert")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.enter_stan)
        navController.navigate(R.id.action_amountInsertFragment4_to_stanInsertFragment2)
    }
}