package com.example.discgolfapp_v1.ui.main

import java.util.*

internal object VirtualBagData {
    val data: TreeMap<String, List<DiscInfo>>
        get() {
            val expandableListDetail = TreeMap<String, List<DiscInfo>>()

            expandableListDetail["Distance Drivers"] = distanceDrivers
            expandableListDetail["Fairway Drivers"] = fairwayDrivers
            expandableListDetail["Mid-Ranges"] = midranges
            expandableListDetail["Putters"] = putters

            return expandableListDetail
        }
    val distanceDrivers: MutableList<DiscInfo> = ArrayList()
    val fairwayDrivers: MutableList<DiscInfo> = ArrayList()
    val midranges: MutableList<DiscInfo> = ArrayList()
    val putters: MutableList<DiscInfo> = ArrayList()
}