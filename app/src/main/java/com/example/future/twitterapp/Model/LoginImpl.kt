package com.example.future.twitterapp.Model

import android.content.Context
import android.widget.Toast
import com.example.future.twitterapp.Presenter.LoginPresenter
import com.example.future.twitterapp.View.LoginView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginImpl : LoginPresenter {
    var mAuth: FirebaseAuth? = null
    private var loginView: LoginView


    constructor(loginView: LoginView) {
        this.loginView = loginView
    }

    override fun LoginWithFireBase(email: String, passWord: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth!!.signInWithEmailAndPassword(email,passWord)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        loginView.onSuccess()
                    }else{
                        loginView.onFailed()
                    }
                }
    }
}



