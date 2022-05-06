package com.cm.payplaza.ecr_sdk_integration.activity.preauth.navigationMenu

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import com.cm.payplaza.ecr_sdk_integration.R
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.finish.FinishPreauthActivity
import com.cm.payplaza.ecr_sdk_integration.activity.preauth.start.PreAuthActivity
import com.cm.payplaza.ecr_sdk_integration.databinding.ExpandibleListItemBinding
import com.cm.payplaza.ecr_sdk_integration.databinding.ExpandibleListTitleBinding
import timber.log.Timber

class PreauthExpendibleLisAdapter(
    private val titles: List<String>,
    private val expandibleList: Map<String, List<String>>,
    private val preauthType: PreauthType
    ): BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return titles.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val title = titles[groupPosition]
        val list = expandibleList[title]
        return list?.size ?: run { 0 }
    }

    override fun getGroup(groupPosition: Int): String {
        return titles[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String? {
        val title = titles[groupPosition]
        val list = expandibleList[title]
        return list?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val listTitle = getGroup(groupPosition)
        val inflater = LayoutInflater.from(parent?.context)
        val binding = ExpandibleListTitleBinding.inflate(inflater)
        binding.expandibleListTitle.text = listTitle
        return binding.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val itemTitle = getChild(groupPosition, childPosition)
        val inflater = LayoutInflater.from(parent?.context)
        val binding = ExpandibleListItemBinding.inflate(inflater)

        val itemIcon = when(childPosition) {
            0 -> R.drawable.icon_pinpad_confirm
            1 -> R.drawable.icon_numpad_confirm
            2 -> R.drawable.icon_pinpad_cancel
            else -> R.drawable.bookmark_bar_bullet_bg
        }
        binding.expandibleListItemIcon.setImageResource(itemIcon)
        binding.expandibleListItemText.text = itemTitle
        val itemlPreauthType = PreauthType.values()[childPosition]
        if(preauthType == itemlPreauthType){
            val textColor = parent?.context?.getColor(R.color.mediumShadeColor) ?: Color.GRAY
            binding.expandibleListItemText.setTextColor(textColor)
            binding.expandibleListItemText.isEnabled = false
        }
        return binding.root
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    fun getItemListener(): ExpandableListView.OnChildClickListener {
        return preauthItemListener
    }

    fun getGroupListener(): ExpandableListView.OnGroupClickListener {
        return preauthGroupListener
    }

    private val preauthItemListener = ExpandableListView.OnChildClickListener { parent, _, _, childPosition, _ ->
        parent.context?.let {
            when(childPosition) {
                PreauthType.START.i -> {
                    Timber.d("Start preauth")
                    if(preauthType != PreauthType.START) PreAuthActivity.start(it)
                }
                PreauthType.CONFIRM.i -> {
                    Timber.d("Confirm preauth")
                    if(preauthType != PreauthType.CONFIRM)  FinishPreauthActivity.start(it, PreauthType.CONFIRM)
                }
                PreauthType.CANCEL.i -> {
                    Timber.d("Cancel preauth")
                    if(preauthType != PreauthType.CANCEL)  FinishPreauthActivity.start(it, PreauthType.CANCEL)
                }
            }
        }
        true
    }

    private val preauthGroupListener = ExpandableListView.OnGroupClickListener { parent, _, _, _ ->
        if(parent.isGroupExpanded(0)) {
            parent.collapseGroup(0)
        } else {
            parent.expandGroup(0)
        }
        true
    }
}