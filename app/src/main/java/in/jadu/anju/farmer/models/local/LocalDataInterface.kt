package `in`.jadu.anju.farmer.models.local

import androidx.lifecycle.LiveData
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes

interface LocalDataInterface {

    suspend fun insertListItemTypes(listItemTypes: ListItemTypes)

    fun getListItemByPhoneNumber(phoneNumber:String): LiveData<List<ListItemTypes>>

}