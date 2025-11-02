package com.example.project_b_security_gardapp

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat
import com.example.project_b_security_gardapp.api.Repo.UserRepository
import com.example.project_b_security_gardapp.api.Retrofit.RetrofitInstance
import com.example.project_b_security_gardapp.api.Services.UserServices
import com.example.project_b_security_gardapp.databinding.ActivityNewGuestFormBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class NewGuestFormActivity : AppCompatActivity() {
    lateinit var activityBinding: ActivityNewGuestFormBinding
    lateinit var repository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityNewGuestFormBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        enableEdgeToEdge()

        repository = UserRepository(RetrofitInstance.getInstance.create(UserServices::class.java))

        val sharedPreferences = getSharedPreferences(Keywords.MYPREFS.toString(), MODE_PRIVATE)
        val token = sharedPreferences.getString(Keywords.USERTOKEN.toString(), null)


        activityBinding.btnCancle.setOnClickListener {
            finish()
        }

        activityBinding.btnSend.setOnClickListener {
            val name = activityBinding.InputFieldName.text.toString()
            val phone = "+91".plus(activityBinding.InputFieldPhone.text.toString())
            val reason = activityBinding.InputFieldDescription.text.toString()
            val noOfGuest = activityBinding.FlatEditText.text.toString()

            // Create a file from a drawable resource
            val drawableId = R.drawable.user_avatar // Replace with your drawable ID
            val bitmap = BitmapFactory.decodeResource(resources, drawableId)
            val file = File(cacheDir, "default_image.png")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()

            val photo1 = file


            if(token != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    var result = repository.sendGuestRequest(token, name, phone, reason, noOfGuest, photo1, null)
                    if(result == 200){
                       withContext(Dispatchers.Main){
                           finish()
                       }
                    }else{
                        //Todo reset all input fields
                    }
                }

            }
        }

    }
}