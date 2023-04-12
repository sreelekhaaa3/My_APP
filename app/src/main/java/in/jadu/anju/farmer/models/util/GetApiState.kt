package `in`.jadu.anju.farmer.models.util

import `in`.jadu.anju.farmer.models.dtos.FarmerAuth

sealed class GetApiState{
    object Loading: GetApiState()
    class Success(val data: List<FarmerAuth>) : GetApiState()
    class Error(val msg: Throwable) : GetApiState()
    object Empty: GetApiState()
}
