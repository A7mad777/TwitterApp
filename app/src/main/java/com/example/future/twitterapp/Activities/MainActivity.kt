package com.example.future.twitterapp.Activities

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.future.twitterapp.Model.PostInfo
import com.example.future.twitterapp.Model.Ticket
import com.example.future.twitterapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tweets_tickets.view.*
import kotlinx.android.synthetic.main.add_ticket.*
import kotlinx.android.synthetic.main.add_ticket.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    var TweetList = ArrayList<Ticket>()
    var adapter: TweetsAdapter? = null
    var lvTweets: ListView? = null
    var dataBase = FirebaseDatabase.getInstance()
    var refrence = dataBase.reference
    var ivPost: ImageView? = null
    var upImage: ImageView? = null
    var ePost: EditText? = null
    var email: String? = null
    var UserId: String? = null
    var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lvTweets = findViewById(R.id.lv)
        ePost = findViewById(R.id.editText3)
        upImage = findViewById(R.id.imageView3)
        var bundle: Bundle = intent.extras
        email = bundle.getString("email")
        UserId = bundle.getString("userId")

        loadPosts()

        TweetList.add(Ticket("0", "him", "url", "add"))
        adapter = TweetsAdapter(this, TweetList)
        lvTweets!!.adapter = adapter

    }

    inner class TweetsAdapter : BaseAdapter {
        var context: Context? = null
        var tweetsList = ArrayList<Ticket>()


        constructor(context: Context?, tweetsList: ArrayList<Ticket>) : super() {
            this.context = context
            this.tweetsList = tweetsList
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myTweet = tweetsList[position]
            if (myTweet.tweetPersonUID.equals("add")) {
                var mView = LayoutInflater.from(context).inflate(R.layout.add_ticket, null)
                mView.imageView3.setOnClickListener(View.OnClickListener {
                    LoadImage()
                })
                mView.imageView2.setOnClickListener(View.OnClickListener {
                    refrence.child("posts").push().setValue(PostInfo(UserId, mView.editText3.text.toString(), DownLoadUrl))

                })
                return mView
            } else {
                var mView = LayoutInflater.from(context).inflate(R.layout.activity_tweets_tickets, null)
                var calendar: Calendar = Calendar.getInstance()
                var simpleDateFormat: SimpleDateFormat = SimpleDateFormat()
                var date: String = simpleDateFormat.format(calendar.time)
                mView.textView5.setText(myTweet.tweetText)
                mView.textView3.setText(myTweet.tweetPersonUID)
                mView.textView4.setText(date)
                Picasso.with(context).load(myTweet.tweetImageUrl).into(mView.imageView5)
                return mView
            }
        }

        override fun getItem(position: Int): Any {
            return tweetsList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return tweetsList.size
        }
    }

    var Pick_Image_Code = 32
    fun LoadImage() {
        var intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, Pick_Image_Code)
    }

    var uri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Pick_Image_Code && data != null) {
            uri = data.data
            var bitmapDrawable = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            var bitmp = BitmapDrawable(bitmapDrawable)
            UpLoadImage(bitmp)
        }
    }

    var DownLoadUrl: String? = null
    fun UpLoadImage(bitmapDrawable: BitmapDrawable) {
        if (uri == null) return
        val fileName = UUID.randomUUID().toString()
        var ref = FirebaseStorage.getInstance().getReference("/imagePost/$fileName")
        ref.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            DownLoadUrl = taskSnapshot.downloadUrl.toString()
        }

    }

    fun loadPosts() {
        refrence.child("posts").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                try {
                    TweetList.clear()
                    TweetList.add(Ticket("0", "him", "url", "add"))
                    var td = p0!!.value as HashMap<String, Any>
                    for (key in td.keys) {
                        var post = td[key] as HashMap<String, Any>
                        TweetList.add(Ticket(key, post["text"] as String, post["downLoadUrl"] as String, post["userId"] as String))
                    }
                    adapter!!.notifyDataSetChanged()
                } catch (ex: Exception) {

                }
            }

        })
    }

}