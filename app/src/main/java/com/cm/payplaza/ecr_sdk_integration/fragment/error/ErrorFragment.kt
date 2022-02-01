package com.cm.payplaza.ecr_sdk_integration.fragment.error

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentErrorBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TransactionError
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import org.koin.android.ext.android.inject
import timber.log.Timber

class ErrorFragment: BaseEcrFragment<ErrorFragmentState, ErrorFragmentViewModel, FragmentErrorBinding>() {
    override val viewModel: ErrorFragmentViewModel by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentErrorBinding {
        return FragmentErrorBinding.inflate(inflater, container, false)
    }

    override fun render(state: ErrorFragmentState) {
        when(state) {
            is ErrorFragmentState.Init -> {
                setUpButton()
                fillTextViews(state.transactionError)
            }
            else -> { }
        }
    }

    private fun setUpButton() {
        Timber.d("setUpButton")
        binding.errorContinueButton.setOnClickListener {
            viewModel.dismiss()
        }
    }

    private fun fillTextViews(transactionError: TransactionError) {
        Timber.d("fillTextViews")
        binding.errorHeader.text = transactionError.desc
        binding.errorFooter.visibility = View.GONE
    }
}