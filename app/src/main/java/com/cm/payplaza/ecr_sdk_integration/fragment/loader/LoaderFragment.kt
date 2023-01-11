package com.cm.payplaza.ecr_sdk_integration.fragment.loader

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cm.payplaza.ecr_sdk_integration.databinding.FragmentLoaderBinding
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import org.koin.android.ext.android.inject

class LoaderFragment: BaseEcrFragment<LoaderFragmentState, LoaderFragmentViewModel, FragmentLoaderBinding>() {
    override val viewModel: LoaderFragmentViewModel by inject()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoaderBinding {
        return FragmentLoaderBinding.inflate(inflater, container, false)
    }

    override fun render(state: LoaderFragmentState) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
