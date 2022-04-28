package com.example.discgolfapp_v1.ui.main

import com.example.discgolfapp_v1.data.Disc
import com.example.discgolfapp_v1.data.DiscViewModel
import java.util.*

class VirtualBagData (discViewModel: DiscViewModel) {
    private val mDiscViewModel = discViewModel
    val data: TreeMap<String, List<Disc>>
        get() {
            val expandableListDetail = TreeMap<String, List<Disc>>()
            val distanceDrivers: MutableList<Disc> = ArrayList()
            val fairwayDrivers: MutableList<Disc> = ArrayList()
            val midranges: MutableList<Disc> = ArrayList()
            val putters: MutableList<Disc> = ArrayList()

            if (mDiscViewModel.readAllData.value != null) {
                for (disc in mDiscViewModel.readAllData.value!!) {
                    when (disc.type) {
                        0 -> distanceDrivers.add(disc)
                        1 -> fairwayDrivers.add(disc)
                        2 -> midranges.add(disc)
                        3 -> putters.add(disc)
                    }
                }
            }

            expandableListDetail["Distance Drivers"] = distanceDrivers
            expandableListDetail["Fairway Drivers"] = fairwayDrivers
            expandableListDetail["Mid-Ranges"] = midranges
            expandableListDetail["Putters"] = putters

            return expandableListDetail
        }
}