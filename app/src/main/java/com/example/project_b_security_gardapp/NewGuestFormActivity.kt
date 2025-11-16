package com.example.project_b_security_gardapp

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project_b_security_gardapp.Adapters.ImageAdapter
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.ActivityNewGuestFormBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class NewGuestFormActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityNewGuestFormBinding
    lateinit var repository: UserRepository
    private val selectedImages = mutableListOf<Uri>()
    private lateinit var adapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityNewGuestFormBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        enableEdgeToEdge()

        repository = UserRepository(RetrofitInstance.getInstance.create(UserServices::class.java))

        val sharedPreferences = getSharedPreferences(Keywords.MYPREFS.toString(), MODE_PRIVATE)
        val token = sharedPreferences.getString(Keywords.USERTOKEN.toString(), null)

        adapter = ImageAdapter(selectedImages)
        activityBinding.ImageViewPreview.layoutManager = GridLayoutManager(this, 2)
        activityBinding.ImageViewPreview.adapter = adapter

        activityBinding.btnCancle.setOnClickListener {
            finish()
        }

        activityBinding.btnAddPhoto.setOnClickListener {
            if(selectedImages.size >= 2){
                Toast.makeText(this, "You can add only 2 images", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            ImagePicker.with(this)
                .galleryMimeTypes(arrayOf("image/*"))
                .start()
        }

        activityBinding.btnSend.setOnClickListener {
            val name = activityBinding.InputFieldName.text.toString()
            val phone = "+91".plus(activityBinding.InputFieldPhone.text.toString())
            val reason = activityBinding.InputFieldDescription.text.toString()
            val noOfGuest = activityBinding.FlatEditText.text.toString()

            // Create a file from a drawable resource
            val drawableId = R.drawable.user_avatar // Replace with your drawable ID
            val bitmap = BitmapFactory.decodeResource(resources, drawableId)

            val files = selectedImages.map { uriToFile(it) }
            val file1 = files.get(0)
            val file2 = files.get(1)

            if(token != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    var result = repository.sendGuestRequest(token, name, phone, reason, noOfGuest,file1 , file2)
                    if(result == 200){
                       withContext(Dispatchers.Main){
                           finish()
                       }
                    }else{
                        //TODO:: reset all input fields
                    }
                }

            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    selectedImages.add(clipData.getItemAt(i).uri)
                }
            } else {
                data?.data?.let { selectedImages.add(it) }
            }
            adapter.notifyDataSetChanged()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "IMG_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}