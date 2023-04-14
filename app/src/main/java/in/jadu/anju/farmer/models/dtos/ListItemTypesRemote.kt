package `in`.jadu.anju.farmer.models.dtos

data class ListItemTypesRemote(
    val productType: String,
    val productName:String,
    val productImageUrl:String,
    val description:String,
    val productPacked:String,
    val productExpire:String,
    val productPrice:String,
)