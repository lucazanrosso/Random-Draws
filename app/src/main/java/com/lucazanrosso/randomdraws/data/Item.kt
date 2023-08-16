package com.lucazanrosso.randomdraws.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "group") val group: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "extracted") var extracted: Boolean,
)

data class Group(
    val groupName: String,
    val groupCount: Int,
    val toBeDrawn: Int
)