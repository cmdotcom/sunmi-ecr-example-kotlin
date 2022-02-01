package com.cm.payplaza.ecr_sdk_integration.uicomponents.amountViewComponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.ComponentAmountViewBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.uicomponents.base.BaseEcrComponment
import org.koin.core.component.inject
import timber.log.Timber
import java.util.*

class AmountViewComponent: BaseEcrComponment<AmountViewComponentViewState, AmountViewComponentViewModel> {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    override val viewModel: AmountViewComponentViewModel by inject()
    private val binding: ComponentAmountViewBinding

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.component_amount_view, this, true)
        binding = ComponentAmountViewBinding.bind(view)
        orientation = VERTICAL
        super.init()
    }

    fun setAmount(newInsertedDigits: Int) {
        Timber.d("setAmount - $newInsertedDigits")
        viewModel.formatAmount(newInsertedDigits)
    }

    override fun render(state: AmountViewComponentViewState) {
        when(state) {
            is AmountViewComponentViewState.Init -> { }
            is AmountViewComponentViewState.UpdateInsertedDigits -> binding.amountViewAmount.text = state.newInsertedDigits
            AmountViewComponentViewState.AddPinToken -> addPinToken()
            AmountViewComponentViewState.ClearView -> binding.amountViewAmount.text = ""
            is AmountViewComponentViewState.SetUpCurrency -> setUpCurrency(state.merchantData)
        }
    }

    private fun setUpCurrency(terminalData: TerminalData?) {
        terminalData?.let {
            terminalData.currency?.let {
                binding.amountViewCurrency.text = terminalData.currency.symbol
            }
        }
    }

    fun configureForAmountInsert() {
        Timber.d("configureForAmountInsert")
        binding.amountViewContainer.visibility = View.VISIBLE
        binding.amountViewCurrency.visibility = View.VISIBLE
        binding.amountViewCalendar.visibility = View.INVISIBLE
        binding.amountViewAmount.text = context.getString(R.string.component_amount_view_initial_amount)
        viewModel.setUpCurrency()
    }

    fun clearView() {
        Timber.d("clearView")
        viewModel.clearView()
    }

    // Password insert
    fun configureForPasswordInsert() {
        Timber.d("configureForPasswordInsert")
        binding.amountViewContainer.visibility = View.GONE
        binding.amountViewAmount.textAlignment = TEXT_ALIGNMENT_CENTER
    }

    fun addPasswordToken() {
        Timber.d("addPasswordToken")
        viewModel.addPinToken()
    }

    // Stan insert
    fun configureForStanInsert() {
        Timber.d("configureForStanInsert")
        binding.amountViewContainer.visibility = View.GONE
        binding.amountViewAmount.textAlignment = TEXT_ALIGNMENT_CENTER
    }
    fun addStanDigit(stanDigits: Int) {
        Timber.d("addStanDigit - $stanDigits")
        viewModel.addStanDigits(stanDigits)
    }

    // Date insert
    fun configureForDateInsert() {
        Timber.d("configureForDateInsert")
        binding.amountViewContainer.visibility = View.VISIBLE
        binding.amountViewCurrency.visibility = View.INVISIBLE
        binding.amountViewCalendar.visibility = View.VISIBLE
        binding.amountViewAmount.textAlignment = TEXT_ALIGNMENT_CENTER
        binding.amountViewAmount.hint = context?.getString(R.string.date_hint)
    }

    fun setDate(date: Date) {
        Timber.d("setDate - $date")
        viewModel.formatDate(date)
    }

    private fun addPinToken() {
        val text = binding.amountViewAmount.text
        "$text*".also { binding.amountViewAmount.text = it }
    }
}