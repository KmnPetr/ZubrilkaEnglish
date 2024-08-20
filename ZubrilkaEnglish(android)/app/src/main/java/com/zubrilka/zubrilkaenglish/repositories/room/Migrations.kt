package com.zubrilka.zubrilkaenglish.repositories.room

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zubrilka.zubrilkaenglish.utils.LOG
import com.zubrilka.zubrilkaenglish.utils.defaultMode

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        migration1to2(database)
    }
}
//надо положить некоторые данные в бд не касающиеся изменения схемы
//функция должна вызваться в двух местах как в миграциях так и при создании бд с нуля в случае которого миграции не выполняются
fun migration1to2(database: SupportSQLiteDatabase){
    database.execSQL("INSERT INTO prop_table (key, value) VALUES ('${PropKey.IS_FIRST_ENTRY.key}', 'true')")
}

val MIGRATION_2_3 = object : Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        migration2to3(database)
    }
}
//надо положить некоторые данные в бд не касающиеся изменения схемы
//функция должна вызваться в двух местах как в миграциях так и при создании бд с нуля в случае которого миграции не выполняются
fun migration2to3(database: SupportSQLiteDatabase){
    database.execSQL("INSERT INTO prop_table (key, value) VALUES ('${PropKey.IS_AGREE_PRIVACY.key}', 'false')")
}

val MIGRATION_3_4 = object : Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        migration3to4(database)
    }
}
//надо положить некоторые данные в бд не касающиеся изменения схемы
//функция должна вызваться в двух местах как в миграциях так и при создании бд с нуля в случае которого миграции не выполняются
fun migration3to4(database: SupportSQLiteDatabase){
    database.execSQL("INSERT INTO prop_table (key, value) VALUES ('${PropKey.learningMode.key}', '$defaultMode')")
}