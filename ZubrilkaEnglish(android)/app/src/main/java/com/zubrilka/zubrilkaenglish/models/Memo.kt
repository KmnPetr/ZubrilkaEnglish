package com.zubrilka.zubrilkaenglish.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Calendar

@Entity(tableName = "memo_table")
class Memo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "hour")
    val hour:Int,
    @ColumnInfo(name = "minutes")
    val minutes:Int,
    @ColumnInfo(name = "note")
    val note:String = "",
    @ColumnInfo(name = "daysOfWeek")
    val daysOfWeek: List<DayOfWeek>
    )

enum class DayOfWeek(val ruStr: String,val weekDay: Int?) {
    DAILY("Ежедневно", null),
    MONDAY("ПН",Calendar.MONDAY),
    TUESDAY("ВТ", Calendar.TUESDAY),
    WEDNESDAY("СР", Calendar.WEDNESDAY),
    THURSDAY("ЧТ", Calendar.THURSDAY),
    FRIDAY("ПТ", Calendar.FRIDAY),
    SATURDAY("СБ", Calendar.SATURDAY),
    SUNDAY("ВС", Calendar.SUNDAY)
}

class Converters {
    @TypeConverter
    fun fromString(value: String): List<DayOfWeek> {
        return value.split(",").map { DayOfWeek.valueOf(it) }
    }

    @TypeConverter
    fun fromList(list: List<DayOfWeek>): String {
        return list.joinToString(",")
    }
}