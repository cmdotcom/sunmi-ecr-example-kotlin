package com.cm.payplaza.ecr_sdk_integration.fragment.receiptView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentReceiptViewBinding
import com.cm.payplaza.ecr_sdk_integration.entity.Receipt
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import org.koin.android.ext.android.inject
import timber.log.Timber

class ReceiptViewFragment : BaseEcrFragment<ReceiptViewFragmentState,
        ReceiptViewFragmentViewModel,
        FragmentReceiptViewBinding>() {
    override val viewModel: ReceiptViewFragmentViewModel by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReceiptViewBinding {
        return FragmentReceiptViewBinding.inflate(inflater, container, false)
    }

    override fun render(state: ReceiptViewFragmentState) {
        when(state) {
            is ReceiptViewFragmentState.Init -> initializeView(state.isPrinterAvailable)
            is ReceiptViewFragmentState.Canceled -> setUpCanceled(state.receipt)
            is ReceiptViewFragmentState.SuccessTwoReceipt -> setUpTwoReceipt(state.customerReceipt, state.merchantReceipt, state.isSuccessful)
            is ReceiptViewFragmentState.SuccessOneReceipt -> setUpOneReceipt(state.receipt, state.isSuccessful)
            is ReceiptViewFragmentState.MerchantReceiptShowed -> setUpOneReceipt(state.receipt, state.isSuccessful)
            else -> { }
        }
    }

    private fun initializeView(isPrinterAvailable: Boolean) {
        setUpPrinter(isPrinterAvailable)
        viewModel.checkReceipt()
    }

    private fun setUpOneReceipt(receipt: Receipt, isSuccesfull: Boolean) {
        if(!isSuccesfull) { switchSuccessIconToWarning() }
        fillReceipt(receipt)
        binding.receiptViewCloseButton.setOnClickListener {
            viewModel.finishTransaction()
        }
        binding.receiptViewButtonPrint.setOnClickListener {
            viewModel.printReceipt(receipt)
            viewModel.finishTransaction()
        }
    }

    private fun setUpTwoReceipt(customerReceipt: Receipt, merchantReceipt: Receipt, isSuccesfull: Boolean) {
        if(!isSuccesfull) { switchSuccessIconToWarning() }
        fillReceipt(merchantReceipt)
        binding.receiptViewCloseButton.setOnClickListener {
            viewModel.merchantReceiptshowed(customerReceipt, isSuccesfull)
        }
        binding.receiptViewButtonPrint.setOnClickListener {
            viewModel.printReceipt(merchantReceipt)
            viewModel.merchantReceiptshowed(customerReceipt, isSuccesfull)
        }
    }

    private fun setUpCanceled(receipt: Receipt) {
        setUpOneReceipt(receipt, isSuccesfull = false)
        switchSuccessIconToWarning()
    }

    private fun fillReceipt(receipt: Receipt) {
        binding.receiptTextComponent.setReceiptData(receipt)
    }

    private fun setUpPrinter(isPrinterAvailable: Boolean) {
        binding.receiptViewButtonPrint.visibility = if(isPrinterAvailable) View.VISIBLE else View.GONE
    }

    private fun switchSuccessIconToWarning() {
        Timber.d("switchSuccessIconToWarning")
        binding.receiptViewResultIcon.background = AppCompatResources.getDrawable(activity as Context, R.drawable.icon_background_error)
        binding.receiptViewResultIcon.setImageResource(R.drawable.icon_exclamation_mark)
    }
}