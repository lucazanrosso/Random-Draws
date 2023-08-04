package com.lucazanrosso.randomdraws.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    /*suspend*/ fun insert(item: Item)

    @Update
    /*suspend*/ fun update(item: Item)

    @Delete
    /*suspend*/ fun delete(item: Item)

    @Query("DELETE from items WHERE `group` = :groupname")
    fun deleteGroup(groupname: String)

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Item
//    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): List<Item>
//    fun getAllItems(): Flow<List<Item>>

//    @MapInfo(keyColumn = "groupname", valueColumn = "count")
//    @Query("SELECT `group` AS groupname, COUNT(*) AS count from items GROUP BY `group` ORDER BY `group` ASC")
////    fun getGroups(): Map<String, Int>
//    fun getGroups(): Flow<Group>

    @Query("SELECT `group` AS groupName, COUNT(*) AS groupCount from items GROUP BY `group` ORDER BY `group` ASC")
//    fun getGroups(): Map<String, Int>
    fun getGroups(): Flow<List<Group>>

//    @Query("SELECT * FROM user WHERE region IN (:regions)")
//    fun loadUsersFromRegions(regions: List<String>): List<User>
}