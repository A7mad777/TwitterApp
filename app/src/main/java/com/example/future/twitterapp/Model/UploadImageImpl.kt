package com.example.future.twitterapp.Model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.example.future.twitterapp.Activities.LoginActivity
import com.example.future.twitterapp.Presenter.UploadImagePresenter
import com.example.future.twitterapp.View.UploadImageView
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class UploadImageImpl : UploadImagePresenter {
    val Pick_Image_Code: Int = 32
    val ReadImage: Int = 32
   private lateinit var context: Context
    private lateinit var loginActivity : LoginActivity
    private var uploadImageView: UploadImageView


    constructor(uploadImageView: UploadImageView) {
        this.uploadImageView = uploadImageView
    }

    var uri: Uri? = null
    override fun UploadImageToFireBase() {
        if (uri == null) return
        val FileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$FileName")
        ref.putFile(uri!!).addOnSuccessListener {
            Log.d("Login Activity", "Successfuly Loaded image")
        }
    }
}

