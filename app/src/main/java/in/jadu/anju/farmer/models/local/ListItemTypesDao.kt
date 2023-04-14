package `in`.jadu.anju.farmer.models.local

import androidx.lifecycle.LiveData
import androidx.room.*
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes

@Dao
interface ListItemTypesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListItem(listItem: ListItemTypes)

    @Update
    suspend fun updateListItem(listItem: ListItemTypes)

    @Delete
    suspend fun deleteListItem(listItem: ListItemTypes)

    @Query("SELECT * FROM list_item_types")
    fun getAllListItems(): LiveData<List<ListItemTypes>>

    @Query("SELECT * FROM list_item_types WHERE phoneNumber = :phoneNumber")
    fun getListItemByPhoneNumber(phoneNumber: String): LiveData<List<ListItemTypes>>
}