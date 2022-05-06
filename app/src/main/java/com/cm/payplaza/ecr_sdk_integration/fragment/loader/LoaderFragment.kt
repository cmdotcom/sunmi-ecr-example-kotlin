package com.cm.payplaza.ecr_sdk_integration.fragment.loader

import android.content.res.TypedArray
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentLoaderBinding
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.androidposintegration.enums.TransactionType
import org.koin.android.ext.android.inject
import java.math.BigDecimal

class LoaderFragment: BaseEcrFragment<LoaderFragmentState, LoaderFragmentViewModel, FragmentLoaderBinding>() {
    override val viewModel: LoaderFragmentViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation(view, R.attr.progressAnimation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoaderBinding {
        return FragmentLoaderBinding.inflate(inflater, container, false)
    }

    override fun render(state: LoaderFragmentState) {
        when(state){
            is LoaderFragmentState.Init -> setUpFooter(state.currency, state.amount, state.type)
            LoaderFragmentState.HideFooter -> hideFooter()
        }
    }

    private fun setUpFooter(currency: String, amount: BigDecimal, type: TransactionType) {
        binding.footer.setAmount(currency, amount)
        binding.footer.setTransactionMethod(type)
    }

    private fun hideFooter() {
        binding.footer.visibility = View.GONE
    }

    private fun startAnimation(view: View, themeAttributeId: Int) {
        val attrs: TypedArray = view.context.theme.obtainStyledAttributes(intArrayOf(themeAttributeId))
        binding.progressIcon.setImageResource(attrs.getResourceId(0, 0))
        val drawable: Drawable = binding.progressIcon.drawable
        if (drawable is Animatable) {
            (drawable as Animatable).start()
        }
        attrs.recycle()
    }
}