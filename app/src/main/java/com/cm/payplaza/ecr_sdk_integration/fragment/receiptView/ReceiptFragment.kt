package com.cm.payplaza.ecr_sdk_integration.fragment.receiptView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish.FinishPreauthActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentReceiptBinding
import com.cm.payplaza.ecr_sdk_integration.dialog.DialogLauncher
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.payplaza.ecr_sdk_integration.uicomponents.bottomAppBarComponent.BottomAppBarComponent
import org.koin.android.ext.android.inject

class ReceiptFragment : BaseEcrFragment<ReceiptState,
        ReceiptViewModel,
        FragmentReceiptBinding>() {

    override val viewModel: ReceiptViewModel by inject()

    private val args: ReceiptFragmentArgs by lazy {
        ReceiptFragmentArgs.fromBundle(requireArguments())
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReceiptBinding = FragmentReceiptBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is FinishPreauthActivity) {
            (activity as FinishPreauthActivity).setUpMenu()
            (activity as FinishPreauthActivity).setMenuStatuses(listOf(Pair(getString(R.string.cancel_payment), false)))
        }
    }

    override fun render(state: ReceiptState) {
        when (state) {
            is ReceiptState.Init -> initializeView()
            is ReceiptState.Canceled -> setUpCanceled(state.receipt)
            is ReceiptState.SuccessTwoReceipt -> setUpTwoReceipt(
                state.customerReceipt,
                state.merchantReceipt,
                state.isSuccessful
            )
            is ReceiptState.SuccessOneReceipt -> setUpOneReceipt(
                state.receipt
            )
            is ReceiptState.MerchantReceiptShowed -> setUpOneReceipt(
                state.receipt
            )
            else -> {}
        }
    }

    private fun initializeView() {
        viewModel.checkReceipt()
    }

    private fun setUpOneReceipt(receipt: Receipt) {
        fillReceipt(receipt, args.dayTotals)
        val popupListener: DialogLauncher.ActionListener = object : DialogLauncher.ActionListener {
            override fun onOkPressed() {
                viewModel.printCardholderReceipt(receipt, this)
            }
            override fun onCancelPressed() {
                viewModel.finishTransaction()
            }
            override fun onDismiss() {}
        }
        val bottomAppBarListener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                viewModel.finishTransaction()
            }
            override fun onPrintButtonPressed() {
                viewModel.printCardholderReceipt(receipt, popupListener)
            }
        }
        viewModel.setUpBottomAppBarListener(bottomAppBarListener)
    }

    private fun setUpTwoReceipt(
        cardholderReceipt: Receipt,
        merchantReceipt: Receipt,
        isSuccesfull: Boolean
    ) {
        fillReceipt(merchantReceipt)
        binding.receiptType.text = getString(R.string.receipt_merchant)
        val popupListener: DialogLauncher.ActionListener = object : DialogLauncher.ActionListener {
            override fun onOkPressed() {
                viewModel.printMerchantReceipt(
                    merchantReceipt,
                    cardholderReceipt,
                    isSuccesfull,
                    this,
                    ::showCardholderReceipt
                )
            }
            override fun onCancelPressed() {
                showCardholderReceipt(cardholderReceipt, isSuccesfull)
            }
            override fun onDismiss() {}
        }
        val bottomAppBarListener = object: BottomAppBarComponent.ClickListener {
            override fun onActionButtonPressed() {
                showCardholderReceipt(cardholderReceipt, isSuccesfull)
            }
            override fun onPrintButtonPressed() {
                viewModel.printMerchantReceipt(merchantReceipt, cardholderReceipt, isSuccesfull, popupListener, ::showCardholderReceipt)
            }
        }
        viewModel.setUpBottomAppBarListener(bottomAppBarListener)
    }

    private fun showCardholderReceipt(cardholderReceipt: Receipt, isSuccesfull: Boolean) {
        binding.receiptType.text = getString(R.string.receipt_cardholder)
        viewModel.merchantReceiptShowed(cardholderReceipt, isSuccesfull)
        binding.receiptViewScroll.scrollY = 0
    }

    private fun setUpCanceled(receipt: Receipt) {
        setUpOneReceipt(receipt)
    }

    private fun fillReceipt(receipt: Receipt, showDayTotalReceipt: Boolean = false) {
        if (showDayTotalReceipt) {
            binding.receiptType.text = getString(R.string.day_totals)
        }
        binding.receiptTextComponent.setReceiptData(receipt, showDayTotalReceipt)
    }
}