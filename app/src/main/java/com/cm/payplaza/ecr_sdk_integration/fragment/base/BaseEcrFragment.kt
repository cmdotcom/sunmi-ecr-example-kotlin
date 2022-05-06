package com.cm.payplaza.ecr_sdk_integration.fragment.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment.BaseEcrFragmentActivity
import org.koin.core.component.KoinComponent
import timber.log.Timber

abstract class BaseEcrFragment<
        FVS: BaseEcrFragmentViewState,
        FVM: BaseEcrFragmentViewModel<FVS>,
        VB: ViewBinding>: Fragment(),
    KoinComponent {
    protected var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        Timber.d("onAttach")
        if(context is BaseEcrFragmentActivity<*>) {
            context.onAttachedFragment(this)
        }
        super.onAttach(context)
    }

    protected open fun init() {
        viewModel.state.observe(viewLifecycleOwner) {
            if (it is FVS) {
                render(it)
            }
        }
        viewModel.init()
    }

    abstract val viewModel: FVM
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun render(state: FVS)
}