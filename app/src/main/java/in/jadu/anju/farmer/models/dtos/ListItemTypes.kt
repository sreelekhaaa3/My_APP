package `in`.jadu.anju.farmer.models.dtos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_item_types")
data class ListItemTypes(
    val productType: String,
    val productName:String,
    val productImageUrl:String,
    val description:String,
    val productPacked:String,
    val productExpire:String,
    val productPrice:String,
    @PrimaryKey
    var phoneNumber: String = ""
)
