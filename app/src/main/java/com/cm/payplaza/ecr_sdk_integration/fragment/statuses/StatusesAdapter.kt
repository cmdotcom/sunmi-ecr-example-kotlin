package com.cm.payplaza.ecr_sdk_integration.fragment.statuses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.RowStatusesBinding
import com.cm.payplaza.ecr_sdk_integration.entity.StatusData
import com.cm.androidposintegration.enums.TransactionResult

class StatusesAdapter(
    private val data: List<StatusData>,
    private val  itemClickedListener: ItemClickedListener):
RecyclerView.Adapter<StatusesAdapter.ViewHolder>(){

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ViewHolder.getViewBinding(LayoutInflater.from(parent.context), parent)
        return ViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val status = data[position]
        holder.bind(status, itemClickedListener)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var binding: RowStatusesBinding = RowStatusesBinding.bind(view)
        private lateinit var statusData: StatusData
        companion object {
            fun getViewBinding(
                inflater: LayoutInflater,
                container: ViewGroup?
            ): RowStatusesBinding {
                return RowStatusesBinding.inflate(inflater, container, false)
            }
        }
        fun bind(statusData: StatusData, itemClickedListener: ItemClickedListener) {
            this.statusData = statusData
            val context = itemView.context
            val amount = String.format(context.getString(R.string.statuses_amount), statusData.amount.toString() + statusData.currency.symbol)
            val result = String.format(context.getString(R.string.statuses_result), statusData.result.name)
            val type = String.format(context.getString(R.string.statuses_type), statusData.type.toString())
            binding.rowStatusesAmount.text = amount
            binding.rowStatusesResult.text = result
            binding.rowStatusesType.text = type
            if(!statusData.receipt.isEmpty()
                || statusData.result == TransactionResult.REQUEST_RECEIPT) {
                binding.rowStatusesReceipt.setOnClickListener { itemClickedListener.onClick(this.statusData) }
            } else {
                binding.rowStatusesReceipt.visibility = View.INVISIBLE
            }
            if(statusData.result == TransactionResult.REQUEST_RECEIPT) {
                itemView.setBackgroundColor(context.getColor(R.color.refundBackgroundColor))
            }
        }

    }

    interface ItemClickedListener { fun onClick(status: StatusData) }
}