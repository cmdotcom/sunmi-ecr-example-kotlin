package com.cm.payplaza.ecr_sdk_integration.activity.preauth.start

import android.content.Context
import android.content.Intent
import android.view.View
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthExpandibleListData
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthType
import com.cm.payplaza.ecr_sdk_integration.activity.transactionResult.TransactionResultActivity
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PreAuthActivity: BaseEcrFragmentActivity<PreAuthViewModel>() {

    companion object {
        fun start(context: Context) {
            Timber.d("goToPreAuthActivity")
            val intent = Intent(context, PreAuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override val viewModel: PreAuthViewModel by viewModel()

    override fun render(state: BaseEcrViewState) {
        when(state) {
            PreAuthViewState.GoToTransactionResult -> TransactionResultActivity.start(this)
        }
        super.render(state)
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when(state) {
            is AmountInsertFragmentState.NextStep -> savePreauth(state.amount)
            ErrorFragmentState.Dismiss -> supportFragmentManager.popBackStack()
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()
        val menu = binding.ecrNavigation.menu
        menu.findItem(R.id.nav_cancel_payment).isEnabled = false
        menu.findItem(R.id.nav_new_preauth).isEnabled = false
        binding.ecrPreauthExpandibleList.apply {
            val adapter = PreauthExpandibleListData.getPreauthListAdapter(context, PreauthType.START)
            setOnChildClickListener(adapter.getItemListener())
            setOnGroupClickListener(adapter.getGroupListener())
            setAdapter(adapter)
            visibility = View.VISIBLE
        }
    }

    override fun setUpBookmark() {
        Timber.d("setUpBookmark")
        binding.ecrBookmarkBar.setSelectedBookmark(1, R.string.bookmark_preauth)
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.preauth_graph
    }

    private fun savePreauth(amount: Int) {
        viewModel.savePreauth(amount)
    }
}