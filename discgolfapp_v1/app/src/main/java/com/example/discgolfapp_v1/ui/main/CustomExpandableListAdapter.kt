package com.example.discgolfapp_v1.ui.main

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.discgolfapp_v1.R
import com.example.discgolfapp_v1.data.Disc
import java.util.*

class CustomExpandableListAdapter internal constructor(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: TreeMap<String, List<Disc>>
) : BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        val disc = this.dataList[this.titleList[listPosition]]!![expandedListPosition]
        var info = disc.name + "\n"

        if (disc.fnspeed != null) {
            info += disc.fnspeed
        } else {
            info += "-"
        }

        info += " | "
        if (disc.fnglide != null) {
            info += disc.fnglide
        } else {
            info += "-"
        }

        info += " | "
        if (disc.fnturn != null) {
            info += disc.fnturn
        } else {
            info += "-"
        }

        info += " | "
        if (disc.fnfade != null) {
            info += disc.fnfade
        } else {
            info += "-"
        }

        return info
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }

        // Set disc info
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.listView)
        expandedListTextView.text = expandedListText
        expandedListTextView.setBackgroundColor(this.dataList[this.titleList[listPosition]]!![expandedListPosition].color)

        return convertView
    }
    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }
    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }
    override fun getGroupCount(): Int {
        return this.titleList.size
    }
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.group_item, null)
        }

        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.groupView)
        listTitleTextView.text = listTitle

        return convertView
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}