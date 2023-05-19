package `in`.jadu.anju.farmer.ui.fragments

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import `in`.jadu.anju.R
import `in`.jadu.anju.databinding.FragmentSuccessListingBinding
import `in`.jadu.anju.kvstorage.KvStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class SuccessListingFragment : Fragment() {
    private lateinit var binding: FragmentSuccessListingBinding
    private lateinit var contractAddress: String
    private lateinit var kvStorage: KvStorage
    private var bitmap: Bitmap? = null
    private var isQrGenerated = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccessListingBinding.inflate(inflater, container, false)
        kvStorage = KvStorage(requireContext())
        contractAddress = kvStorage.storageGetString("contractAddress")!!
        val productId = arguments?.getString("productId")
        val content = "$contractAddress-$productId"
        Log.d("content", content)
        generateQr(content)
        binding.btnSuccess.setOnClickListener {
            findNavController().navigate(R.id.action_successListingFragment_to_farmerDashBoardFragment2)
        }
        binding.lottieAnimationView.repeatCount = LottieDrawable.INFINITE
        binding.downloadQrCode.setOnClickListener {
            if (isQrGenerated) {
                binding.dnldProgress.visibility = View.VISIBLE
                binding.downloadQrCode.text = ""
                Log.d("bitmap", "$bitmap")
                downloadQRCode(requireContext(), bitmap!!,productId!!)
            }else{
                Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun generateQr(content:String){
        try {
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400)
            binding.productQr.setImageBitmap(bitmap)
            isQrGenerated = true
            binding.productQr.visibility = View.VISIBLE
        } catch (e: Exception) {
            Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun downloadQRCode(context: Context, bitmap: Bitmap,productId:String) {
        val filename = "$productId.png"
        // Get the directory for saving the image (external storage directory)
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(directory, filename)

        // Save the bitmap to the image file
        lifecycleScope.launch(Dispatchers.IO){
            var outputStream: OutputStream? = null
            try {
                outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                withContext(Dispatchers.Main){
                    binding.dnldProgress.visibility = View.GONE
                    binding.downloadQrCode.text = getString(R.string.redownloaded)
                    displaySnackBar(getString(R.string.qr_downloaded))
                }
            } catch (e: Exception) {
                Log.d("downloadQRCode", "Failed to download QR Code", e)
                withContext(Dispatchers.Main){
                    displaySnackBar(getString(R.string.something_went_wrong))
                    binding.dnldProgress.visibility = View.GONE
                    binding.downloadQrCode.text = getString(R.string.retryDownload)
                }
            } finally {
                outputStream?.close()
            }
        }
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            arrayOf("image/png"),
            null
        )
    }
    private fun displaySnackBar(msg: String) {
        val snackBar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            msg,
            Snackbar.LENGTH_LONG
        )
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_text_color))
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.primary_text_color))
        snackBar.show()
    }


}