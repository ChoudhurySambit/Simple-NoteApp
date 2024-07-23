package com.example.samplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samplenoteapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shashank.sony.fancytoastlib.FancyToast

class MainActivity : AppCompatActivity() {

    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth=FirebaseAuth.getInstance()

        binding.createNote.setOnClickListener {

            startActivity(Intent(this,NewNode::class.java))


        }

        binding.openNotes.setOnClickListener {

            FancyToast.makeText(this,"Opening All Notes Of The User ",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()

            startActivity(Intent(this,AllNotes::class.java))
        }

        binding.logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            FancyToast.makeText(this,"Successfully Logged Out ",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true)
            startActivity(Intent(this,SignIn::class.java))
            finish()
        }
    }
}