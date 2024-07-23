package com.example.samplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WelcomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        supportActionBar?.hide()

        findViewById<Button>(R.id.welcomeSignInBtn).setOnClickListener {
            startActivity(Intent(this,SignIn::class.java))
        }
        findViewById<Button>(R.id.welcomeSignUpBtn).setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }
    }
}