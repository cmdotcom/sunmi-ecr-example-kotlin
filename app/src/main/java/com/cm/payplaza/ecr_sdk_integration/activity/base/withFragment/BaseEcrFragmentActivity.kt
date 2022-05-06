package com.cm.payplaza.ecr_sdk_integration.activity.base.withFragment

import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.cm.payplaza.ecr_sdk_integration.activity.base.BaseEcrActivity
import com.cm.payplaza.ecr_sdk_integration.entity.TerminalData
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragment
import com.cm.payplaza.ecr_sdk_integration.fragment.base.BaseEcrFragmentViewState
import timber.log.Timber

abstract class BaseEcrFragmentActivity<
        VM: BaseEcrFragmentActivityViewModel>: BaseEcrActivity<VM>() {
    protected lateinit var navController: NavController

    fun onAttachedFragment(fragment: Fragment?) {
        Timber.d("onAttachedFragment - ${fragment?.javaClass?.simpleName}")
        (fragment  as BaseEcrFragment<*,*,*>).viewModel.state.observe(this) {
            if (it is BaseEcrFragmentViewState) {
                renderFragment(it)
            }
        }
    }

    override fun initializeView(terminalData: TerminalData?) {
        super.initializeView(terminalData)
        navController = binding.ecrViewFragment.findNavController()
        navController.setGraph(getNavigationGraph())
    }

    override fun onBackPressed() {
        if (binding.ecrDrawer.isDrawerOpen(GravityCompat.START)) {
            Timber.d("getActivityNavController")
            binding.ecrDrawer.closeDrawer(GravityCompat.START)
        } else {
            Timber.d("getActivityNavController")
            super.onBackPressed()
        }
    }

    protected abstract fun renderFragment(state: BaseEcrFragmentViewState)
    protected abstract fun getNavigationGraph(): Int
}