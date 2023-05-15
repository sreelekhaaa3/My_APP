package `in`.jadu.anju.farmer.viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.jadu.anju.farmer.models.dtos.ListItemTypes
import `in`.jadu.anju.farmer.models.dtos.Product
import `in`.jadu.anju.farmer.models.local.LocalDataInterface
import `in`.jadu.anju.farmer.models.repository.FarmerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.bouncycastle.jce.provider.BouncyCastleProvider
import retrofit2.HttpException
import java.io.File
import java.security.Provider
import java.security.Security
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FarmerListItemViewModel @Inject constructor(
    private val farmerRepository: FarmerRepository,
    private val LocalDataRepository: LocalDataInterface,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application
) :
    ViewModel() {
    private val mainEventChannel = Channel<MainEvent>()
    val mainEvent = mainEventChannel.receiveAsFlow()
    private var currentUser: FirebaseUser? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _index = 0
    private var _imageData: MultipartBody.Part? = null
    private var _uri: Uri? = null
    var locality = ""

    private val _getFarmerListData = MutableLiveData<List<Product>>()
    val getFarmerListData: LiveData<List<Product>>
        get() = _getFarmerListData

    init {
        setupBouncyCastle()
    }

    val getItems = LocalDataRepository.getListItemByPhoneNumber("")
    fun setPhone(phoneNo: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            farmerRepository.setPhone(phoneNo)
        } catch (e: HttpException) {
            e.printStackTrace()
            viewModelScope.launch {
                mainEventChannel.send(MainEvent.Error(e.message()))
            }
        }

    }

    fun checkLocationPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(application, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(application, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
    }

    fun getLastLocation(){
        if(checkLocationPermission()){
            val task = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener {
                if (it != null) {
                    getFarmLocationFromCoordinates(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun getFarmLocationFromCoordinates(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(application, Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(latitude, longitude, 1) as List<Address>
        locality = addresses[0].getAddressLine(0)
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
        } catch (e: HttpException) {
            e.printStackTrace()
            viewModelScope.launch {
                mainEventChannel.send(MainEvent.Error(e.message()))
            }
        }

    }

    suspend fun insertListItemTypes(listItemTypes: ListItemTypes) =
        viewModelScope.launch(Dispatchers.IO) {
            LocalDataRepository.insertListItemTypes(listItemTypes)
        }

    fun getListItemByPhoneNumber(phoneNumber: String) {
        LocalDataRepository.getListItemByPhoneNumber(phoneNumber)
    }

    fun setIndex(index: Int) {
        _index = index
    }

    fun getIndex(): Int {
        return _index
    }

    fun setImageMultipart(imageData: MultipartBody.Part) {
        _imageData = imageData
    }

    fun getImageMultipart(): MultipartBody.Part? {
        return _imageData
    }

    fun setUri(uri: Uri) {
        _uri = uri
    }

    fun getUri(): Uri? {
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
        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val nameIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
        val fileName = cursor?.getString(nameIndex!!)
        cursor?.close()

        // Construct the actual file path
        val path = Environment.getExternalStorageDirectory().absolutePath + "/Download/" + fileName
        Log.d("FarmerListItemViewModel", "getRealPathFromUri: $path")

        return path
    }

    sealed class MainEvent {
        data class Error(val error: String) : MainEvent()
        data class Success(val message: String) : MainEvent()
    }

    private fun setupBouncyCastle() {
        val provider: Provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            return
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }

    fun isManageStoragePermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return result == PackageManager.PERMISSION_GRANTED
        }
    }
}