package com.example.discgolfapp_v1.ui.main

class DiscInfo(dId: Int,
               dName: String,
               dColor: Int,
               dType: Int,
               dImageFile: String? = null,
               dFlightNums: Array<Int?>? = null,
               dWeight: Int? = null,
               dManufacturer: String? = null,
               dPlastic: String? = null,
               dNotes: String? = null
) {
    var id: Int = dId
    var name: String = dName
    var discColor: Int = dColor
    var type: Int = dType
    var imageFile: String? = dImageFile
    var flightNums: Array<Int?>? = dFlightNums?.clone()
    var weight: Int? = dWeight
    var manufacturer: String? = dManufacturer
    var plastic: String? = dPlastic
    var notes: String? = dNotes

    fun getTextInfo(): String {
        var info = name

        if (flightNums != null) {
            info += "\n"
            if (flightNums!![0] != null) {
                info += flightNums!![0]
            } else {
                info += "-"
            }

            info += " | "
            if (flightNums!![1] != null) {
                info += flightNums!![1]
            } else {
                info += "-"
            }

            info += " | "
            if (flightNums!![2] != null) {
                info += flightNums!![2]
            } else {
                info += "-"
            }

            info += " | "
            if (flightNums!![3] != null) {
                info += flightNums!![3]
            } else {
                info += "-"
            }
        }

        return info
    }
}