package com.example.discgolfapp_v1.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.discgolfapp_v1.R

class ThrowListAdapter(context: Context, throws: ArrayList<ThrowInfo>) :
    ArrayAdapter<ThrowInfo>(context, 0, throws) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val throwItem = getItem(position)
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.throw_list_item, parent, false)
        }

        val discTextView = view!!.findViewById<TextView>(R.id.throw_disc_name)
        val throwTypeTextView = view.findViewById<TextView>(R.id.throw_type)
        val notesTextView = view.findViewById<TextView>(R.id.throw_notes)
        val throwDistTextView = view.findViewById<TextView>(R.id.throw_distance)

        discTextView.text = throwItem!!.discDisplayName
        throwTypeTextView.text = throwItem.type
        notesTextView.text = throwItem.notes
        throwDistTextView.text = "${throwItem.distance} ft"

        if (throwItem.flight == view.resources.getStringArray(R.array.throw_flights)[0]) {
            throwTypeTextView.text = throwItem.type
        }
        else {
            throwTypeTextView.text = "${throwItem.type} ${throwItem.flight}"
        }

        return view
    }
}