package `in`.jadu.anju.farmer.models.dtos

data class Product(
    val __v: Int,
    val _id: String,
    val creator: String,
    val description: String,
    val productExpire: String,
    val productImageUrl: String,
    val productName: String,
    val productPacked: String,
    val productPrice: String,
    val productPurchased: List<Any>,
    val productType: String,
    val web3Id: String,
    val contractAddress:String
)