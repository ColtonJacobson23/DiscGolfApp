package com.example.discgolfapp_v1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.discgolfapp_v1.data.DiscViewModel
import com.example.discgolfapp_v1.ui.main.CustomExpandableListAdapter
import com.example.discgolfapp_v1.ui.main.VirtualBagData

class InventoryActivity : AppCompatActivity() {
    private lateinit var mDiscViewModel: DiscViewModel
    private lateinit var virtualBagData: VirtualBagData
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        mDiscViewModel = ViewModelProvider(this).get(DiscViewModel::class.java)
        mDiscViewModel.readAllData.observe(this, Observer {
            expandableListView = findViewById(R.id.virtualBagList)
            if (expandableListView != null) {
                val listData = virtualBagData.data
                titleList = ArrayList(listData.keys)
                adapter =
                    CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
                expandableListView!!.setAdapter(adapter)

                expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                    Toast.makeText(
                        applicationContext,
                        "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(
                                titleList as
                                        ArrayList<String>
                                )
                                [groupPosition]]!![childPosition].name,
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            }
        })
        virtualBagData = VirtualBagData(mDiscViewModel)

        expandableListView = findViewById(R.id.virtualBagList)
        if (expandableListView != null) {
            val listData = virtualBagData.data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)

            expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                Toast.makeText(
                    applicationContext,
                    "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(
                            titleList as
                                    ArrayList<String>
                            )
                            [groupPosition]]!![childPosition].name,
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        }
    }

    fun startAddDiscActivity(view: View) {
        val intent = Intent(this, AddDiscActivity::class.java)
        startActivity(intent)
    }
}