package com.example.future.twitterapp.Activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.example.future.twitterapp.Model.LoginImpl
import com.example.future.twitterapp.Model.Ticket
import com.example.future.twitterapp.Model.UploadImageImpl
import com.example.future.twitterapp.Presenter.LoginPresenter
import com.example.future.twitterapp.Presenter.UploadImagePresenter
import com.example.future.twitterapp.R
import com.example.future.twitterapp.R.id.*
import com.example.future.twitterapp.View.LoginView
import com.example.future.twitterapp.View.UploadImageView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_login.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(),LoginView,UploadImageView,View.OnClickListener{
    var ivPerson: ImageView? = null
    val ReadImg: Int = 32
    var emailText: EditText? = null
    var passWordText: EditText? = null
    var btnUpload: Button? = null
    var btnLogin: Button? = null
    var loginPresenter : LoginPresenter ?= null
    var LoginImpl : LoginImpl ?= null
    private var database = FirebaseDatabase.getInstance()
    private var RefData = database.reference
    var mAuth: FirebaseAuth? = null
    var storage: FirebaseStorage? = null
    var loginView : LoginView ?= null
    var storageReference: StorageReference? = null
    val Pick_imag : Int = 32
   lateinit var  uploadImagePresenter : UploadImagePresenter
    var UserId : String ?= null
    var typeface : Typeface ?= null
    var textView : TextView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ivPerson = findViewById(R.id.imageView)
        emailText = findViewById(R.id.editText)
        passWordText = findViewById(R.id.editText2)
        btnLogin = findViewById(R.id.button)
        typeface = Typeface.createFromAsset(assets,"Billabong.ttf")
        Mtitle.typeface = typeface

        btnLogin!!.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()
        loginPresenter = LoginImpl(this)
        uploadImagePresenter = UploadImageImpl(this)

        ivPerson!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                CheckPermission()
            }
        })

    }
    override fun onClick(v: View?) {
        loginPresenter!!.LoginWithFireBase(emailText!!.text.toString(),passWordText!!.text.toString())
        uploadImageToFireBase()
    }
    override fun onSuccess() {
        Toast.makeText(applicationContext,"Login Success",Toast.LENGTH_LONG).show()
        var currentUser = mAuth!!.currentUser
        if (currentUser != null){
            var intent = Intent(this,MainActivity::class.java)
            intent.putExtra("userId",currentUser.uid)
            intent.putExtra("email",currentUser.email)
            startActivity(intent)
        }
    }

    override fun onFailed() {
        Toast.makeText(applicationContext,"Login Failed",Toast.LENGTH_LONG).show()
    }
    var uri : Uri ?= null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if (requestCode == Pick_imag && data != null){
           uri = data.data
           val bitmapDrawable = MediaStore.Images.Media.getBitmap(contentResolver,uri)
           val bitmap = BitmapDrawable(bitmapDrawable)
           ivPerson!!.setImageDrawable(bitmap)
       }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            ReadImg -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Cannot Access ypur Image", Toast.LENGTH_LONG).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    fun CheckPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), Pick_imag)
                return
            }
        }
        LoadImage()
    }
    fun LoadImage(){
        val intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,Pick_imag)
    }
    fun uploadImageToFireBase(){
        if (uri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(uri!!)
                .addOnSuccessListener { taskSnapshot ->
                    Log.d("RegisterActivity","successfully uploaded image:")
                }
    }
}

