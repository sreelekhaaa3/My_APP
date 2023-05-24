package `in`.jadu.anju.farmer.models.dtos


data class RequestedProduct(
    val _id: String,
    val contractAddress: String,
    val date: String,
    val paymentAmount: String,
    val product: List<ProductX>,
    val userAddress: String,
    val userId: String,
    val userName: String,
    val userPhoneNo: String,
    val web3Id: String
)