package com.zubrilka.zubrilkaenglish.repositories.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zubrilka.zubrilkaenglish.models.PropModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PropDAO {
    @Query("SELECT*FROM prop_table WHERE prop_table.`key`='dictionary_version'")
    suspend fun getDictionaryVersion(): PropModel?
    @Query("UPDATE prop_table SET value=:newDictionaryVersion WHERE prop_table.`key` ='dictionary_version'")
    suspend fun updateDictionaryVersion(newDictionaryVersion: String?)
    @Query("INSERT OR REPLACE INTO prop_table ('key', value) VALUES ('dictionary_version', :newDictionaryVersion)")
    suspend fun insertNewDictionaryVersion(newDictionaryVersion: String?)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewProp(propModel: PropModel)
    @Query("SELECT*FROM prop_table WHERE prop_table.`key`= :key")
    suspend fun getPropByKey(key: String): PropModel?

    @Query("SELECT*FROM prop_table WHERE prop_table.`key`= 'profile'")
    fun getProfile(): Flow<PropModel?>
    @Query("DELETE FROM prop_table WHERE prop_table.`key`= :key")
    suspend fun deletePropByKey(key: String)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrCreateProperties(propModel: PropModel)
    @Query("SELECT*FROM prop_table")
    fun getAllProperties(): Flow<List<PropModel>>
}

/**
 * этот энам хранит некоторые названия ключей,
 * чтобы соовсем не забыть что в БД лежит
 */
enum class PropKey(val key: String) {
    catalogFilter_hideLearned("catalogFilter_hideLearned"),//пользовательская фильтровка слов в каталоге хранит boolean
    catalogFilter_hideSleepingAndActive("catalogFilter_hideSleepingAndActive"),//пользовательская фильтровка слов в каталоге хранит boolean
    isUserHasOwnMemos("isUserHasOwnMemos"), //содержит "true" если пользователь раньше уже создавал memo напоминания
    learningMode("learningMode"), //режим обучения, например на честность или многовариантный выбор
    IS_FIRST_ENTRY("IS_FIRST_ENTRY"), //укажет что юзер вошел первый раз в приложение
    IS_AGREE_PRIVACY("IS_AGREE_PRIVACY") //укажет, что человек уже соглашался с политикой конфеденциальности
}