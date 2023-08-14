package com.lucazanrosso.randomdraws.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Query("UPDATE items SET name = :name WHERE id = :id")
    suspend fun updateName(id: Int, name: String)

    @Query("UPDATE items SET `group` = :newGroup WHERE `group` = :previuosGroup")
    suspend fun updateGroup(previuosGroup: String, newGroup: String)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE from items WHERE name = ''")
    suspend fun deleteVoidItems()

    @Query("DELETE from items WHERE `group` = :groupName")
    suspend fun deleteGroup(groupName: String)

    @Query("SELECT * from items WHERE `group` = :groupName ORDER BY name ASC")
    fun getGroupDetails(groupName: String): Flow<List<Item>>

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT `group` AS groupName, COUNT(*) AS groupCount from items GROUP BY `group` ORDER BY `group` ASC")
    fun getGroups(): Flow<List<Group>>

}