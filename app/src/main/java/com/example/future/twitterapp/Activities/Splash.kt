package com.example.future.twitterapp.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.future.twitterapp.R

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        val background = object : Thread(){
            override fun run() {
              try {
                  Thread.sleep(1000)
                  val intent = Intent(baseContext,LoginActivity::class.java)
                  startActivity(intent)
              }catch (e : Exception){
                  e.printStackTrace()
              }
            }
        }
        background.start()
    }
}


