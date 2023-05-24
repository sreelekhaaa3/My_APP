package `in`.jadu.anju.farmer.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import `in`.jadu.anju.R
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.models.dtos.Product
import `in`.jadu.anju.farmer.models.dtos.RemoteListTypeBackend
import `in`.jadu.anju.utils.UtilityFunctions


class FarmerListAdapter(private val itemTypes: List<Product>,private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FarmerListAdapter.FarmerListViewHolder>() {


    inner class FarmerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val productType: TextView = itemView.findViewById(R.id.tv_product_type)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productImage: ImageView = itemView.findViewById(R.id.iv_vegetables)
        val productDescription: TextView = itemView.findViewById(R.id.tv_description_text)
        val tvExpiryDate: TextView = itemView.findViewById(R.id.tv_expire_date)
        val price: TextView = itemView.findViewById(R.id.farmer_price)
        val itemListCardView:CardView = itemView.findViewById(R.id.item_list_card_view)
        val rl_product_parent:RelativeLayout = itemView.findViewById(R.id.ImageRelativeLayout)
        val imageCardView:CardView = itemView.findViewById(R.id.image_card_view)
        val address:TextView = itemView.findViewById(R.id.farmer_location)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val productData = itemTypes[position]
                    listener.onItemClicked(position,productData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmerListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_of_farmer, parent, false)
        return FarmerListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FarmerListViewHolder, position: Int) {
        val currentItem = itemTypes[position]
//        holder.productType.text = currentItem.productType.removeSurrounding("\"")
        holder.productName.text = currentItem.productName.removeSurrounding("\"")
        holder.productDescription.text = currentItem.description.removeSurrounding("\"")
        holder.tvExpiryDate.text = currentItem.productExpire.removeSurrounding("\"")
        holder.price.text = "₹" + currentItem.productPrice.removeSurrounding("\"") + "/Kg"
        //use glide to set the image here
        val imageLink = getImageLink(currentItem.productImageUrl.removeSurrounding("\""))
        UtilityFunctions.getImageBitmap(holder.itemView.context, imageLink) { bitmap ->
            if(bitmap!=null){
                Palette.from(bitmap).generate {palette->
                    val dominantColor = palette?.getLightVibrantColor(ContextCompat.getColor(holder.itemView.context,R.color.blueColor))
                    if(dominantColor!=null){
                        holder.itemListCardView.setCardBackgroundColor(dominantColor)
                        holder.imageCardView.setCardBackgroundColor(dominantColor)
                    }
                }
            }
        }
        Glide.with(holder.itemView.context).load(imageLink).into(holder.productImage)

    }

    override fun getItemCount(): Int {
        return itemTypes.size
    }

    private fun getImageLink(imgId: String): String {
        val baseUrl = "https://firebasestorage.googleapis.com/v0/b/productserver-57d88.appspot.com/"
        val imagePath = "o/$imgId?alt=media"
        return baseUrl + imagePath
    }



    interface OnItemClickListener {
        fun onItemClicked(position: Int, productData: Product)
    }
}