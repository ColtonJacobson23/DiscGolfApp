package com.example.discgolfapp_v1.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    "throw_table",
    foreignKeys = [
        ForeignKey(
            entity = Disc::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("discId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Throw (
    @PrimaryKey(autoGenerate=true)
    val id: Int,
    val discId: Int,

    var type: String,
    var flight: String,
    var notes: String?,
    var distance: Int
)