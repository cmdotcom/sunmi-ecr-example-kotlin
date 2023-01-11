package com.cm.payplaza.ecr_sdk_integration.uicomponents.amountViewComponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.ComponentAmountViewBinding
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.uicomponents.base.BaseEcrComponment
import org.koin.core.component.inject
import timber.log.Timber
import java.util.*

class AmountViewComponent :
    BaseEcrComponment<AmountViewComponentViewState, AmountViewComponentViewModel> {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

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
        when (state) {
            is AmountViewComponentViewState.Init -> {}
            is AmountViewComponentViewState.UpdateInsertedDigits -> updateInsertedDigits(state.newInsertedDigits)
            AmountViewComponentViewState.AddPinToken -> addPinToken()
            AmountViewComponentViewState.ClearView -> clearAmountView()
            is AmountViewComponentViewState.SetUpCurrency -> setUpCurrency(state.merchantData)
        }
    }

    fun configureForAmountInsert() {
        Timber.d("configureForAmountInsert")
        binding.amountViewContainer.visibility = View.VISIBLE
        binding.amountViewCurrency.visibility = View.VISIBLE
        binding.amountViewCalendar.visibility = View.INVISIBLE
        binding.amountViewAmount.text =
            context.getString(R.string.component_amount_view_initial_amount)
        viewModel.setUpCurrency()
    }

    fun clearView() {
        Timber.d("clearView")
        viewModel.clearView()
    }

    // Password insert
    fun configureForPasswordInsert() {
        Timber.d("configureForPasswordInsert")
        binding.amountViewCurrencyContainer.visibility = View.GONE
        binding.amountViewDivider.visibility = View.GONE
        binding.amountViewCalendar.visibility = View.GONE
        binding.amountViewCurrency.visibility = View.GONE
        binding.amountViewAmount.textAlignment = TEXT_ALIGNMENT_CENTER
        binding.amountViewContainer.visibility = View.VISIBLE
        disableAmountViewShape()
    }

    fun addPasswordToken() {
        Timber.d("addPasswordToken")
        viewModel.addPinToken()
    }

    // Stan insert
    fun configureForStanInsert() {
        Timber.d("configureForStanInsert")
        binding.amountViewCurrencyContainer.visibility = View.GONE
        binding.amountViewDivider.visibility = View.GONE
        binding.amountViewCalendar.visibility = View.GONE
        binding.amountViewCurrency.visibility = View.GONE
        binding.amountViewAmount.textAlignment = TEXT_ALIGNMENT_CENTER
        binding.amountViewContainer.visibility = View.VISIBLE
        disableAmountViewShape()
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

    private fun clearAmountView() {
        binding.amountViewAmount.text = ""
    }

    private fun updateInsertedDigits(newInsertedDigits: String) {
        binding.amountViewAmount.text = newInsertedDigits
    }

    fun enableAmountViewShape() {
        binding.amountViewContainer.background =
            AppCompatResources.getDrawable(context, R.drawable.shape_selected_amount_view)
    }

    fun disableAmountViewShape() {
        binding.amountViewContainer.background =
            AppCompatResources.getDrawable(context, R.drawable.shape_amount_view)
    }

    private fun setUpCurrency(terminalData: TerminalData?) {
        terminalData?.currency?.let {
            binding.amountViewCurrency.text = it.symbol
            binding.amountViewAmount.text = getAmountPlaceHolder(it)
        }
    }

    private fun getAmountPlaceHolder(currency: Currency): String {
        var placeHolder = "0"

        if (currency.defaultFractionDigits > 0) {
            placeHolder = "0."
        }
        for (i in 0 until currency.defaultFractionDigits) {
            placeHolder += "0"
        }

        return placeHolder
    }
}
