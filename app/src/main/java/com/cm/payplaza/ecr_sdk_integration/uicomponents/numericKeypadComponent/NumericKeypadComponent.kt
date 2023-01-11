package com.cm.payplaza.ecr_sdk_integration.uicomponents.numericKeypadComponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.ComponentNumericKeypadBinding
import com.cm.payplaza.ecr_sdk_integration.uicomponents.base.BaseEcrComponment
import org.koin.core.component.inject
import timber.log.Timber

class NumericKeypadComponent :
    BaseEcrComponment<NumericKeypadComponentState, NumericKeypadComponentViewModel> {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    interface ClickListener {
        fun onKeypadDigitPressed(digit: Int)
        fun onKeypadBackspacePressed()
    }

    override val viewModel: NumericKeypadComponentViewModel by inject()
    private val binding: ComponentNumericKeypadBinding
    private var listener: ClickListener = object : ClickListener {
        override fun onKeypadDigitPressed(digit: Int) {}
        override fun onKeypadBackspacePressed() {}
    }

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.component_numeric_keypad, this, true)
        binding = ComponentNumericKeypadBinding.bind(view)
        orientation = VERTICAL
        super.init()
    }

    override fun render(state: NumericKeypadComponentState) {
        when (state) {
            NumericKeypadComponentState.Init -> setUpKeyPad()
            NumericKeypadComponentState.DisableBackSpaceAndConfirm -> disableBackSpaceAndConfirm()
            NumericKeypadComponentState.EnableBackSpaceAndConfirm -> enableBackSpaceAndConfirm()
            NumericKeypadComponentState.EnableNumericKeypad -> setIsEnableKeypad(true)
            NumericKeypadComponentState.DisableNumericKeypad -> setIsEnableKeypad(false)
            NumericKeypadComponentState.EnableBackspace -> setIsEnableBackspaceButton(true)
            NumericKeypadComponentState.ClearKeypad -> {
                setIsEnableKeypad(true)
                disableBackspaceAndConfirm()
            }
        }
    }

    private fun enableBackSpaceAndConfirm() {
        Timber.d("enableBackSpaceAndConfirm")
        setIsEnableBackspaceButton(true)
    }

    private fun disableBackSpaceAndConfirm() {
        Timber.d("disableBackSpaceAndConfirm")
        setIsEnableBackspaceButton(false)
    }

    fun setKeypadListener(newListener: ClickListener) {
        Timber.d("setKeypadListener")
        this.listener = newListener
    }

    fun disableBackspaceAndConfirm() {
        Timber.d("disableBackspaceAndConfirm")
        viewModel.disableBackspaceAndConfirm()
    }

    fun enableBackspaceAndConfirm() {
        Timber.d("enableBackspaceAndConfirm")
        viewModel.enableBackspaceAndConfirm()
    }

    fun disableNumericKeypad() {
        Timber.d("disableNumericKeypad")
        viewModel.disableNumericKeypad()
    }

    fun enableNumericKeypad() {
        Timber.d("enableNumericKeypad")
        viewModel.enableNumericKeypad()
    }

    fun clearKeypad() {
        Timber.d("clearKeypad")
        viewModel.clearKeypad()
    }

    fun enableBackspace() {
        Timber.d("enableBackspace")
        viewModel.enableBackspace()
    }

    private fun setUpKeyPad() {
        Timber.d("setUpKeyPad")
        setIsEnableKeypad(true)
        addClickListenersToKeypad()
        setIsEnableBackspaceButton(false)
        updateButtonsAlpha()
    }

    private fun setIsEnableKeypad(isEnable: Boolean) {
        Timber.d("setIsEnableKeypad - $isEnable")
        binding.keypadButton0.isEnabled = isEnable
        binding.keypadButton1.isEnabled = isEnable
        binding.keypadButton2.isEnabled = isEnable
        binding.keypadButton3.isEnabled = isEnable
        binding.keypadButton4.isEnabled = isEnable
        binding.keypadButton5.isEnabled = isEnable
        binding.keypadButton6.isEnabled = isEnable
        binding.keypadButton7.isEnabled = isEnable
        binding.keypadButton8.isEnabled = isEnable
        binding.keypadButton9.isEnabled = isEnable
        updateButtonsAlpha()
    }

    private fun addClickListenersToKeypad() {
        Timber.d("addClickListenersToKeypad")
        binding.keypadButton0.setOnClickListener { listener.onKeypadDigitPressed(0) }
        binding.keypadButton1.setOnClickListener { listener.onKeypadDigitPressed(1) }
        binding.keypadButton2.setOnClickListener { listener.onKeypadDigitPressed(2) }
        binding.keypadButton3.setOnClickListener { listener.onKeypadDigitPressed(3) }
        binding.keypadButton4.setOnClickListener { listener.onKeypadDigitPressed(4) }
        binding.keypadButton5.setOnClickListener { listener.onKeypadDigitPressed(5) }
        binding.keypadButton6.setOnClickListener { listener.onKeypadDigitPressed(6) }
        binding.keypadButton7.setOnClickListener { listener.onKeypadDigitPressed(7) }
        binding.keypadButton8.setOnClickListener { listener.onKeypadDigitPressed(8) }
        binding.keypadButton9.setOnClickListener { listener.onKeypadDigitPressed(9) }
        binding.keypadButtonBackspace.setOnClickListener { listener.onKeypadBackspacePressed() }
    }

    private fun setIsEnableBackspaceButton(isEnable: Boolean) {
        Timber.d("setIsEnableBackspaceButton - $isEnable")
        binding.keypadButtonBackspace.isEnabled = isEnable
        updateButtonsAlpha()
    }

    private fun updateButtonsAlpha() {
        Timber.d("updateButtonsAlpha")
        binding.keypadButtonBackspace.alpha =
            if (binding.keypadButtonBackspace.isEnabled) 1f else .25f
        binding.keypadButton0.alpha = if (binding.keypadButton0.isEnabled) 1f else .25f
        binding.keypadButton1.alpha = if (binding.keypadButton1.isEnabled) 1f else .25f
        binding.keypadButton2.alpha = if (binding.keypadButton2.isEnabled) 1f else .25f
        binding.keypadButton3.alpha = if (binding.keypadButton3.isEnabled) 1f else .25f
        binding.keypadButton4.alpha = if (binding.keypadButton4.isEnabled) 1f else .25f
        binding.keypadButton5.alpha = if (binding.keypadButton5.isEnabled) 1f else .25f
        binding.keypadButton6.alpha = if (binding.keypadButton6.isEnabled) 1f else .25f
        binding.keypadButton7.alpha = if (binding.keypadButton7.isEnabled) 1f else .25f
        binding.keypadButton8.alpha = if (binding.keypadButton8.isEnabled) 1f else .25f
        binding.keypadButton9.alpha = if (binding.keypadButton9.isEnabled) 1f else .25f
    }
}
