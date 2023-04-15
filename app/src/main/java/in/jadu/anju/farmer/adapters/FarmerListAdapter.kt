package `in`.jadu.anju.farmer.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.jadu.anju.R
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.models.dtos.Product
import `in`.jadu.anju.farmer.models.dtos.RemoteListTypeBackend


class FarmerListAdapter(private val itemTypes: List<Product>) : RecyclerView.Adapter<FarmerListAdapter.FarmerListViewHolder>() {


//    private var farmerList = emptyList<ListItemTypes>()

    class FarmerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productType: TextView = itemView.findViewById(R.id.tv_product_type)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productImage: ImageView = itemView.findViewById(R.id.iv_vegetables)
        val productDescription: TextView = itemView.findViewById(R.id.tv_description_text)
        val tvExpiryDate:TextView = itemView.findViewById(R.id.tv_expire_date)
        val price:TextView = itemView.findViewById(R.id.farmer_price)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmerListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_of_farmer, parent, false)
        return FarmerListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FarmerListViewHolder, position: Int) {
        val currentItem = itemTypes[position]
        holder.productType.text = currentItem.productType.removeSurrounding("\"")
        holder.productName.text = currentItem.productName.removeSurrounding("\"")
        holder.productDescription.text = currentItem.description.removeSurrounding("\"")
        holder.tvExpiryDate.text = currentItem.productExpire.removeSurrounding("\"")
        holder.price.text = "₹"+currentItem.productPrice.removeSurrounding("\"") + "/Kg"
        //use glide to set the image here
        val imageLink = getImageLink(currentItem.productImageUrl.removeSurrounding("\""))
        Log.d("RohitImagelik", imageLink)
        Glide.with(holder.itemView.context).load(imageLink).into(holder.productImage)

    }

    override fun getItemCount(): Int {
        Log.d("Rohit", itemTypes.size.toString())
        return itemTypes.size
    }

    private fun getImageLink(imgId:String): String {
        val baseUrl = "https://firebasestorage.googleapis.com/v0/b/productserver-57d88.appspot.com/"
        val imagePath = "o/$imgId?alt=media"
        return baseUrl + imagePath
    }


}