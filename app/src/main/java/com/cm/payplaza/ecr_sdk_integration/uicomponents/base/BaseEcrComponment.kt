package com.cm.payplaza.ecr_sdk_integration.uicomponents.base

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import org.koin.core.component.KoinComponent

abstract class BaseEcrComponment<CS: BaseEcrComponentViewState, CVM: BaseEcrComponentViewModel<CS>>: LinearLayoutCompat, KoinComponent {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    protected fun init() {
        if (isInEditMode) return
        viewModel.state.observe(context as AppCompatActivity, {
            if(it is CS) {
                render(it)
            }
        })
        viewModel.init()
    }
    protected abstract val viewModel: CVM
    protected abstract fun render(state: CS)
}