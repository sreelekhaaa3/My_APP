package `in`.jadu.anju.farmer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.jadu.anju.R
import `in`.jadu.anju.farmer.models.dtos.RequestedProduct


class ReceivedOrderAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<ReceivedOrderAdapter.ReceivedOrderViewHolder>() {

    var receivedProduct: RequestedProduct? = null
    inner class ReceivedOrderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val productName: TextView = itemView.findViewById(R.id.cart_product_name_text)
        val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedOrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_requested_list,parent,false)
        return ReceivedOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return receivedProduct?.product?.size ?: 0
    }

    override fun onBindViewHolder(holder: ReceivedOrderViewHolder, position: Int) {
        val currentItem = receivedProduct?.product?.get(position)
        holder.productName.text = currentItem?.product?.productName?.removeSurrounding("\"")
        val imageLink = currentItem?.product?.productImageUrl?.removeSurrounding("\"")
            ?.let { getImageLink(it) }
        Glide.with(holder.itemView.context).load(imageLink).into(holder.ivProductImage)
    }
    private fun getImageLink(imgId: String): String {
        val baseUrl = "https://firebasestorage.googleapis.com/v0/b/productserver-57d88.appspot.com/"
        val imagePath = "o/$imgId?alt=media"
        return baseUrl + imagePath
    }
    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }
}