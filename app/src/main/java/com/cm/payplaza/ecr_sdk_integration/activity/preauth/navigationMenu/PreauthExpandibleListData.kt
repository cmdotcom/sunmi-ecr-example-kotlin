package com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu

import android.content.Context
import com.cm.payplaza.ecr_sdk_integration.R

class PreauthExpandibleListData {
    companion object {
        private fun getPreauthGroupTitle(context: Context): String {
            return context.getString(R.string.preauth)
        }

        private fun getPreauthMenuList(context: Context): List<String> {
            return listOf(
                context.resources.getString(R.string.preauth_item_start),
                context.resources.getString(R.string.preauth_item_confirm),
                context.resources.getString(R.string.preauth_item_cancel),
            )
        }

        private fun getPreauthListMap(context: Context): Map<String, List<String>> {
            return mapOf(getPreauthGroupTitle(context)  to getPreauthMenuList(context))
        }

        fun getPreauthListAdapter(context: Context, preauthType: PreauthType): PreauthExpendibleLisAdapter {
            return PreauthExpendibleLisAdapter(
                listOf(getPreauthGroupTitle(context)),
                getPreauthListMap(context),
                preauthType
            )
        }
    }
}