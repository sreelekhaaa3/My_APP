package `in`.jadu.anju.farmer.models.repository

import androidx.lifecycle.LiveData
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.models.local.ListItemTypesDao
import `in`.jadu.anju.farmer.models.local.LocalDataInterface

class LocalDataRepository(private val listItemTypesDao: ListItemTypesDao):LocalDataInterface {
    override suspend fun insertListItemTypes(listItemTypes: ListItemTypes) {
        return listItemTypesDao.insertListItem(listItemTypes)
    }

    override fun getListItemByPhoneNumber(phoneNumber: String): LiveData<List<ListItemTypes>> {
        return listItemTypesDao.getListItemByPhoneNumber(phoneNumber)
    }

}