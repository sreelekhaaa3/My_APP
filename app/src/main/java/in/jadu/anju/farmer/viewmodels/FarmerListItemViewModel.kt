package `in`.jadu.anju.farmer.viewmodels

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.models.dtos.Product
import `in`.jadu.anju.farmer.models.dtos.RemoteListTypeBackend
import `in`.jadu.anju.farmer.models.local.LocalDataInterface
import `in`.jadu.anju.farmer.models.repository.FarmerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.bouncycastle.jce.provider.BouncyCastleProvider
import retrofit2.HttpException
import java.io.File
import java.security.Provider
import java.security.Security
import javax.inject.Inject

@HiltViewModel
class FarmerListItemViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository,
    private val LocalDataRepository: LocalDataInterface
) :
    ViewModel() {
    private val mainEventChannel = Channel<MainEvent>()
    val mainEvent = mainEventChannel.receiveAsFlow()
    private var currentUser: FirebaseUser? = null
    private  var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _index = 0
    private var _imageData: MultipartBody.Part? = null
    private var _uri: Uri? = null

    private val _getFarmerListData = MutableLiveData<List<Product>>()
    val getFarmerListData: LiveData<List<Product>>
        get() = _getFarmerListData

    init {
        setupBouncyCastle()
    }

    val getItems = LocalDataRepository.getListItemByPhoneNumber("")
    fun setPhone(phoneNo: String) = viewModelScope.launch(Dispatchers.IO) {
        farmerRepository.setPhone(phoneNo)
    }

    fun getFarmerItemList(phoneNo: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = farmerRepository.farmerProductList(phoneNo)
            if (response.isSuccessful) {
                Log.d("FarmerListItemViewModel", "getFarmerItemList: ${response.body()}")
                _getFarmerListData.postValue(response.body()?.product)

            }

        } catch (e: HttpException) {
            e.printStackTrace()
            viewModelScope.launch {
                mainEventChannel.send(MainEvent.Error(e.message()))
            }
        }
    }

    fun createProductRemote(
        productType: String,
        productName: String,
        productImageUri: MultipartBody.Part,
        description: String,
        productPacked: String,
        productExpire: String,
        productPrice: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            farmerRepository.createProduct(
                productType,
                productName,
                productImageUri,
                description,
                productPacked,
                productExpire,
                productPrice,
                auth.currentUser?.phoneNumber.toString().substring(3)
            )
            viewModelScope.launch {
                mainEventChannel.send(MainEvent.Success("Product Created Successfully"))
            }
        }catch (e:HttpException){
            e.printStackTrace()
            viewModelScope.launch {
                mainEventChannel.send(MainEvent.Error(e.message()))
            }
        }

    }

    suspend fun insertListItemTypes(listItemTypes: ListItemTypes) = viewModelScope.launch(Dispatchers.IO) {
        LocalDataRepository.insertListItemTypes(listItemTypes)
    }

    fun getListItemByPhoneNumber(phoneNumber: String) {
        LocalDataRepository.getListItemByPhoneNumber(phoneNumber)
    }

    fun setIndex(index:Int){
        _index = index
    }

    fun getIndex():Int{
        return _index
    }

    fun setImageMultipart(imageData:MultipartBody.Part){
        _imageData = imageData
    }

    fun getImageMultipart():MultipartBody.Part?{
        return _imageData
    }

    fun setUri(uri:Uri){
        _uri = uri
    }

    fun getUri():Uri?{
        return _uri
    }

    fun getImagePart(selectedImageUri: Uri, context: Context): MultipartBody.Part {
        val file = File(getRealPathFromUri(selectedImageUri, context))
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(selectedImageUri)
        val requestFile = RequestBody.create(mimeType?.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("productImageUrl", file.name, requestFile)
    }

    private fun getRealPathFromUri(uri: Uri, context: Context): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path = columnIndex?.let { cursor.getString(it) } ?: ""
        cursor?.close()
        Log.d("pathaddress", "getRealPathFromUri: $path")
        return path
    }
    sealed class MainEvent {
        data class Error(val error: String) : MainEvent()
        data class Success(val message: String) : MainEvent()
    }

    private fun setupBouncyCastle() {
        val provider: Provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?:
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            return
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
}