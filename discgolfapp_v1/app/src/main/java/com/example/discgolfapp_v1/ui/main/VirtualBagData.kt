package com.example.discgolfapp_v1.ui.main

import java.util.*

internal object VirtualBagData {
    val data: TreeMap<String, List<String>>
        get() {
            val expandableListDetail = TreeMap<String, List<String>>()

            val distanceDrivers: MutableList<String> = ArrayList()
            val fairwayDrivers: MutableList<String> = ArrayList()
            val midranges: MutableList<String> = ArrayList()
            val putters: MutableList<String> = ArrayList()

            distanceDrivers.add("Thunderbird")
            fairwayDrivers.add("Leopard3")
            midranges.add("Buzzz")
            putters.add("Link")

            expandableListDetail["Distance Drivers"] = distanceDrivers
            expandableListDetail["Fairway Drivers"] = fairwayDrivers
            expandableListDetail["Mid-Ranges"] = midranges
            expandableListDetail["Putters"] = putters

            return expandableListDetail
        }
}