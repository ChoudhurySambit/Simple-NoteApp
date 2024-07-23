package com.example.samplenoteapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.samplenoteapp.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shashank.sony.fancytoastlib.FancyToast

class SignIn : AppCompatActivity() {

    private val binding: ActivitySignInBinding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                account?.let {
                    val credential = GoogleAuthProvider.getCredential(it.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            val user = auth.currentUser
                            user?.let {

                                FancyToast.makeText(this, "Done", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()

                                FancyToast.makeText(this, "Done", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        } else {
                            FancyToast.makeText(this, "Failed: ${authTask.exception?.message}", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
                        }
                    }
                }
            } else {
                FancyToast.makeText(this, "Failed: ${task.exception?.message}", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
            }
        } else {
            FancyToast.makeText(this, "Failed ${result.resultCode}", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("612195098953-8anfsg5vvedslkg3rk30tr7ldreper4c.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signInBtn.setOnClickListener {
            val userId = binding.signInUserId.text.toString()
            val password = binding.signInPassword.text.toString().trim()

            if (userId.isEmpty() && password.isEmpty()) {
                FancyToast.makeText(this, "Please Fill All The Field", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show()
            } else {
                auth.signInWithEmailAndPassword(userId, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        FancyToast.makeText(this, "User Successfully Signed In", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        FancyToast.makeText(this, "User Doesn't Exist", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show()
                    }
                }
            }
        }

        binding.signInsignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        binding.googleBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            Log.d("SignIn", "Launching sign-in intent")
            launcher.launch(signInIntent)
        }
    }
}
