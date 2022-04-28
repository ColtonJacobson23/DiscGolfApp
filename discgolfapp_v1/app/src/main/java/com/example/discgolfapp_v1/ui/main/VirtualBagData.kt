package com.example.discgolfapp_v1.ui.main

import com.example.discgolfapp_v1.data.Disc
import com.example.discgolfapp_v1.data.DiscViewModel
import java.util.*

class VirtualBagData (discViewModel: DiscViewModel) {
    private val mDiscViewModel = discViewModel
    val data: TreeMap<String, List<Disc>>
        get() {
            val expandableListDetail = TreeMap<String, List<Disc>>()

            expandableListDetail["Distance Drivers"] = mDiscViewModel.distanceDrivers.value ?: ArrayList()
            expandableListDetail["Fairway Drivers"] = mDiscViewModel.fairwayDrivers.value ?: ArrayList()
            expandableListDetail["Mid-Ranges"] = mDiscViewModel.midranges.value ?: ArrayList()
            expandableListDetail["Putters"] = mDiscViewModel.putters.value ?: ArrayList()

            return expandableListDetail
        }
}