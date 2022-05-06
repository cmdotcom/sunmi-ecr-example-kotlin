package com.cm.payplaza.ecr_sdk_integration.fragment.statuses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentStatusesBinding
import com.cm.payplaza.ecr_sdk_integration.entity.StatusData
import com.cm.payplaza.ecr_sdk_integration.entity.StatusesData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.androidposintegration.enums.TransactionResult
import org.koin.android.ext.android.inject
import timber.log.Timber

class StatusesFragment: BaseEcrFragment<
        StatusesFragmentState,
        StatusesFragmentViewModel,
        FragmentStatusesBinding>(),
StatusesAdapter.ItemClickedListener {
    override val viewModel: StatusesFragmentViewModel by inject()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStatusesBinding {
        return FragmentStatusesBinding.inflate(inflater, container, false)
    }

    override fun render(state: StatusesFragmentState) {
        when(state) {
            is StatusesFragmentState.Init -> initializeView(state.data)
            else -> { }
        }
    }

    private fun initializeView(data: StatusesData) {
        val adapter = StatusesAdapter(data.data, this)
        binding.statusesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.statusesRecyclerView.adapter = adapter
        binding.statusesBackButton.setOnClickListener { viewModel.continueToNewPayment() }
    }

    override fun onClick(status: StatusData) {
        Timber.d("onClick - $status")
        if(status.result == TransactionResult.REQUEST_RECEIPT) { viewModel.lastReceipt() }
        else { viewModel.showReceipt(status) }
    }
}