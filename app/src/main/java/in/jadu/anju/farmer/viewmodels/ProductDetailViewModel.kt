package `in`.jadu.anju.farmer.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor():ViewModel() {


   fun getImageBitmap(context: Context, imgUrl: String, callback: (Bitmap?) -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(imgUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Optional: handle when the resource is cleared
                }
            })
    }
    fun getImageLink(imgId: String): String {
        val baseUrl = "https://firebasestorage.googleapis.com/v0/b/productserver-57d88.appspot.com/"
        val imagePath = "o/$imgId?alt=media"
        return baseUrl + imagePath
    }
}