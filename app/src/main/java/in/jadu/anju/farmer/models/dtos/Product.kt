package `in`.jadu.anju.farmer.models.dtos

import android.os.Parcel
import android.os.Parcelable

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
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<Any>().apply {
            parcel.readList(this, Any::class.java.classLoader)
        },
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(__v)
        parcel.writeString(_id)
        parcel.writeString(creator)
        parcel.writeString(description)
        parcel.writeString(productExpire)
        parcel.writeString(productImageUrl)
        parcel.writeString(productName)
        parcel.writeString(productPacked)
        parcel.writeString(productPrice)
        parcel.writeList(productPurchased)
        parcel.writeString(productType)
        parcel.writeString(web3Id)
        parcel.writeString(contractAddress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}