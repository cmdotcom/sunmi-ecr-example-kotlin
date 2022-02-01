package com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrActivity
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import timber.log.Timber

abstract class BaseEcrFragmentActivity<
        VM: BaseEcrFragmentActivityViewModel,
        VB: ViewBinding>: BaseEcrActivity<VM, VB>() {
    protected lateinit var navController: NavController

    fun onAttachedFragment(fragment: Fragment?) {
        Timber.d("onAttachedFragment - ${fragment?.javaClass?.simpleName}")
        (fragment  as BaseEcrFragment<*,*,*>).viewModel.state.observe(this, {
            if(it is BaseEcrFragmentViewState) {
                renderFragment(it)
            }
        })
    }

    protected abstract fun renderFragment(state: BaseEcrFragmentViewState)
    protected abstract fun getActivityNavController(): NavController
}