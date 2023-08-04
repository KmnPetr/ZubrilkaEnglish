package com.example.zubrilkaenglish.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prop_table")
data class PropModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "key")
    val key:String,
    @ColumnInfo(name = "value")
    val value:String
){
    override fun toString(): String {
        return "PropModel('$key', '$value')"
    }
}
