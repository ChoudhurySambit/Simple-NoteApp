package com.example.samplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samplenoteapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.shashank.sony.fancytoastlib.FancyToast

class SignUp : AppCompatActivity() {

    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

    override fun onStart() {
        super.onStart()
        val currentUser=auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        Firebase.auth.signOut()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth=FirebaseAuth.getInstance()

        binding.signUpBtn.setOnClickListener {
            val userName=binding.userName.text.toString()
            val userEmailId=binding.UserEmailId.text.toString()
            val userPhoneNumber=binding.userPhoneNumber.text.toString()
            val password=binding.userPassword.text.toString()
            val confirmPassword=binding.userConfirmPassword.text.toString()

            if(userName.isBlank() && userEmailId.isBlank() && userPhoneNumber.isBlank() && password.isBlank() && confirmPassword.isBlank() ){

                FancyToast.makeText(this,"Please Fill All The Fields With Appropriate Values",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show()

            }
            else if(password.trim() != confirmPassword.trim()){

                FancyToast.makeText(this,"Password And Confirm Password Doesn't Match",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show()

            }else{

                auth.createUserWithEmailAndPassword(userEmailId,password).addOnCompleteListener {

                    val currentUser=auth.currentUser
                    currentUser?.let {userId->

                        databaseReference=FirebaseDatabase.getInstance().reference
                        databaseReference.child("Users").child(userId.uid).setValue(UserData(userName,userEmailId,userPhoneNumber,password)).addOnSuccessListener {

                            FancyToast.makeText(this,"Successfully Registered",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show()

                            startActivity(Intent(this,SignIn::class.java))
                            finish()

                        }.addOnFailureListener {

                            FancyToast.makeText(this,"Data Storing Failed :${it.suppressedExceptions}",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show()
                        }

                    }
                }
            }

        }

        binding.signUpsignIn.setOnClickListener {

            startActivity(Intent(this,SignIn::class.java))
            finish()

        }

    }
}