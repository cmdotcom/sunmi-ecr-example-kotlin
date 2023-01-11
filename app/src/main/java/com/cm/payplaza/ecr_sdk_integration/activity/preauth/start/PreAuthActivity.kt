package com.cm.payplaza.ecr_sdk_integration.activity.preauth.start

import android.content.Context
import android.content.Intent
import android.view.View
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrViewState
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.MenuItemsDataHolder
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu.PreauthType
import com.cm.payplaza.ecr_sdk_integration.activity.transactionResult.TransactionResultActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.amountInsert.AmountInsertFragmentState
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import com.cm.payplaza.ecr_sdk_integration.fragment.error.ErrorFragmentState
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PreAuthActivity : BaseEcrFragmentActivity<PreAuthViewModel>() {

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
        when (state) {
            PreAuthViewState.GoToTransactionResult -> TransactionResultActivity.start(this)
        }
        super.render(state)
    }

    override fun renderFragment(state: BaseEcrFragmentViewState) {
        when (state) {
            is AmountInsertFragmentState.NextStep -> savePreauth(state.amount)
            is AmountInsertFragmentState.SetupBottomAppBar -> initializePrintButtonForAmountInsert(state.listener)
            is AmountInsertFragmentState.AddDigitToAmountView -> binding.bottomAppView.enableActionButton()
            AmountInsertFragmentState.ClearAmount -> binding.bottomAppView.disableActionButton()
            ErrorFragmentState.Dismiss -> supportFragmentManager.popBackStack()
        }
    }

    override fun setUpMenu() {
        super.setUpMenu()

        with(binding.navigationLayout.ecrPreauthExpandibleList) {
            val adapter = MenuItemsDataHolder.getListAdapter(context, PreauthType.START)
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
                    PreauthType.START
                )
            }
            setAdapter(adapter)
            visibility = View.VISIBLE

            setMenuStatuses(
                listOf(
                    Pair(getString(R.string.cancel_payment), false),
                    Pair(getString(R.string.preauth), false)
                )
            )
        }
    }

    override fun getNavigationGraph(): Int {
        return R.navigation.preauth_graph
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        initializeTransactionType()
    }

    private fun savePreauth(amount: Int) {
        viewModel.savePreauth(amount)
    }

    private fun initializePrintButtonForAmountInsert(listener: BottomAppBarComponent.ClickListener) {
        binding.bottomAppView.setActionButtonText(R.string.bottom_app_bar_card_payment_continue)
        binding.bottomAppView.setTransactionTypeText(R.string.bottom_app_bar_start_pre_auth)
        binding.bottomAppView.setButtonsListeners(listener)
        binding.bottomAppView.disableActionButton()
    }

    private fun initializeTransactionType() {
        binding.bottomAppView.setTransactionTypeText(R.string.bottom_app_bar_start_pre_auth)
    }
}