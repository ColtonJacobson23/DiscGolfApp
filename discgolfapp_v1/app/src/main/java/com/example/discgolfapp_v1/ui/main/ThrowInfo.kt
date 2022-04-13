package com.example.discgolfapp_v1.ui.main

class ThrowInfo(tDiscId: Int,
                tDiscName: String,
                tType: String,
                tFlight: String,
                tNotes: String?,
                tDistance: Int
) {
    var discId: Int = tDiscId
    var discDisplayName: String = tDiscName
    var type: String = tType
    var flight: String = tFlight
    var notes: String? = tNotes
    var distance: Int = tDistance
}