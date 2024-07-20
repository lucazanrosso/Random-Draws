package com.lucazanrosso.randomdraws.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Upsert
    suspend fun upsert(item: Item)

    @Delete
    suspend fun delete(item: Item)

//    @Query("SELECT * from items ORDER BY name ASC")
//    fun getAllItems(): Flow<List<Item>>

    @Query("INSERT INTO items (`group`, name, extracted)" +
            "SELECT :newGroupName, name, extracted " +
            "from items " +
            "WHERE `group` = :groupName")
    suspend fun duplicateGroup(groupName: String, newGroupName: String)

    @Query("SELECT `group` AS groupName, COUNT(*) AS groupCount, SUM(items.extracted = '0') AS toBeDrawn from items GROUP BY `group` ORDER BY `group` ASC")
    fun getGroups(): Flow<List<Group>>


//    @Query("SELECT * from items WHERE `group` = :groupName ORDER BY name ASC")
//    fun getGroupDetails(groupName: String): Flow<List<Item>>

    @Query("SELECT * from items WHERE `group` = :groupName ORDER BY name ASC")
    fun getGroupDetails(groupName: String): Flow<List<Item>>

    /*@Query("SELECT * from items WHERE `group` = :groupName ORDER BY name ASC")
    fun getGroupDetails(groupName: String): /*Flow<*/List<Item>/*>*/*/

    @Query("SELECT * from items WHERE `group` = :groupName AND extracted = 0 ORDER BY name ASC")
    fun getNotExtractedMembers(groupName: String): Flow<List<Item>>

    @Query("SELECT * from items WHERE `group` = :groupName AND extracted = 1 ORDER BY name ASC")
    fun getExtractedMembers(groupName: String): Flow<List<Item>>

    @Query("DELETE from items WHERE `group` = :groupName")
    suspend fun deleteGroup(groupName: String)


}