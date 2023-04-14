package `in`.jadu.anju.farmer.models.local

import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes

@Database(entities = [ListItemTypes::class], version = 1)
abstract class ItemListDatabase:RoomDatabase() {
    abstract fun listItemDao():ListItemTypesDao
}