package com.example.discgolfapp_v1.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity ("disc_table")
data class Disc (
    @PrimaryKey(autoGenerate=true)
    val id: Int,

    val name: String,
    val photo: String?,
    val color: Int,
    val fnspeed: Int?,
    val fnglide: Int?,
    val fnturn: Int?,
    val fnfade: Int?,
    val type: Int,
    val weight: Int?,
    val manufacturer: String?,
    val plastic: String?,
    val notes: String?
)