package com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.databinding.ExpandibleListItemBinding
import com.cm.payplaza.ecr_sdk_integration.databinding.ExpandibleListTitleBinding

class MenuItemsAdapter(
    private val items: List<SubMenuItem>,
    private val groupChildMap: Map<String, List<SubMenuItem>>,
    private val preAuthType: PreauthType,
) : BaseExpandableListAdapter() {

    override fun getGroupCount() = items.size

    override fun getChildrenCount(groupPosition: Int): Int {
        val childrenList = groupChildMap[items[groupPosition].title]
        return childrenList?.size ?: run { 0 }
    }

    override fun getGroup(groupPosition: Int) = items[groupPosition]

    override fun getChild(groupPosition: Int, childPosition: Int): SubMenuItem? {
        val childrenList = groupChildMap[items[groupPosition].title]
        return childrenList?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds() = false

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val groupItem = getGroup(groupPosition)
        val binding = ExpandibleListTitleBinding.inflate(LayoutInflater.from(parent?.context))
        return with(binding) {
            expandibleListTitle.text = groupItem.title
            icSlidingDrawer.setImageResource(groupItem.icon)

            divider.visibility = if (groupPosition == items.lastIndex) View.VISIBLE else View.GONE

            expandibleListTitle.isEnabled = groupItem.isEnabled
            icSlidingDrawer.isEnabled = groupItem.isEnabled
            root
        }
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val childItem = getChild(groupPosition, childPosition)
        val binding = ExpandibleListItemBinding.inflate(LayoutInflater.from(parent?.context))

        return with(binding) {
            expandibleListItemIcon.setImageResource(childItem?.icon ?: 0)
            expandibleListItemText.text = childItem?.title.orEmpty()
            val itemPreAuthType = PreauthType.values()[childPosition]
            if (preAuthType == itemPreAuthType) {
                val textColor = parent?.context?.getColor(R.color.black_40) ?: Color.GRAY
                expandibleListItemText.setTextColor(textColor)
                expandibleListItemText.isEnabled = false
                expandibleListItemIcon.isEnabled = false
            }
            root
        }
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int) = true

    fun setMenuItemStatuses(updatedItems: List<Pair<String, Boolean>>) {
        for (updatedItem in updatedItems) {
            items.forEach {
                if (it.title == updatedItem.first) {
                    it.isEnabled = updatedItem.second
                }
            }
        }
        notifyDataSetChanged()
    }

    fun isMenuItemEnabled(item: MenuItem) : Boolean {
        val itemTextId = convert(item)
        items.forEach {
            if (itemTextId == it.textId && it.isEnabled) {
                return true
            }
        }
        return false
    }
}
