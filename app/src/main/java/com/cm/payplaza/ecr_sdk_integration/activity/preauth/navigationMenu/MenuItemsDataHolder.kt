package com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu

import android.content.Context
import com.cm.payplaza.ecr_sdk_integration.R

object MenuItemsDataHolder {

    fun getListAdapter(
        context: Context,
        preAuthType: PreauthType,
    ) = MenuItemsAdapter(
        getGroupTitles(context),
        getPreAuthListMap(context),
        preAuthType,
    )

    private fun getGroupTitles(context: Context): List<SubMenuItem> {
        return with(context) {
            listOf(
                SubMenuItem(
                    R.string.payment,
                    getString(R.string.payment),
                    R.drawable.ic_payment_selector
                ),
                SubMenuItem(
                    R.string.preauth,
                    getString(R.string.preauth),
                    R.drawable.ic_pre_auth_selector
                ),
                SubMenuItem(
                    R.string.refund,
                    getString(R.string.refund),
                    R.drawable.ic_refund_selector
                ),
                SubMenuItem(
                    R.string.print_last_receipt,
                    getString(R.string.print_last_receipt),
                    R.drawable.ic_receipt_selector
                ),
                SubMenuItem(
                    R.string.day_totals,
                    getString(R.string.day_totals),
                    R.drawable.ic_day_totals_selector
                ),
                SubMenuItem(
                    R.string.request_info,
                    getString(R.string.request_info),
                    R.drawable.ic_download_parameters_selector
                ),
                SubMenuItem(
                    R.string.cancel_payment,
                    getString(R.string.cancel_payment),
                    R.drawable.ic_cancel_payment_selector
                ),
            )
        }
    }

    private fun getPreAuthChildItemsList(context: Context): List<SubMenuItem> {
        return with(context) {
            listOf(
                SubMenuItem(
                    R.string.preauth_item_start,
                    getString(R.string.preauth_item_start),
                    R.drawable.ic_start_selector
                ),
                SubMenuItem(
                    R.string.preauth_item_confirm,
                    getString(R.string.preauth_item_confirm),
                    R.drawable.ic_check_selector
                ),
                SubMenuItem(
                    R.string.preauth_item_cancel,
                    getString(R.string.preauth_item_cancel),
                    R.drawable.ic_close_selector
                )
            )
        }
    }

    private fun getPreAuthListMap(context: Context) = mapOf(
        context.getString(R.string.preauth) to getPreAuthChildItemsList(context),
    )
}