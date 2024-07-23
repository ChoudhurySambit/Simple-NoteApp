package com.example.samplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samplenoteapp.databinding.ActivityNewNodeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast

class NewNode : AppCompatActivity() {

    private val binding: ActivityNewNodeBinding by lazy {
        ActivityNewNodeBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.saveNote.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val noteTitle = binding.noteTitle.text.toString()
        val noteDescription = binding.noteDescription.text.toString()

        if (noteTitle.isEmpty() and noteDescription.isEmpty()) {
            FancyToast.makeText(
                this, "Please Fill All The Fields",
                FancyToast.LENGTH_LONG,
                FancyToast.WARNING, true
            ).show()
        } else {
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                val noteKey = databaseReference.child("Users").child(user.uid).child("Notes").push().key
                val noteItem = NoteItems(noteTitle, noteDescription,noteKey?:"")

                noteKey?.let {
                    databaseReference.child("Users").child(user.uid).child("Notes")
                        .child(it).setValue(noteItem)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                FancyToast.makeText(
                                    this, "Note Saved Successfully",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS, true
                                ).show()

                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                FancyToast.makeText(
                                    this, "Failed To Save The Note",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.ERROR, true
                                ).show()
                            }
                        }
                } ?: run {
                    FancyToast.makeText(
                        this, "Failed To Generate Note Key",
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR, true
                    ).show()
                }
            } ?: run {
                FancyToast.makeText(
                    this, "User Not Logged In",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR, true
                ).show()
            }
        }
    }
}
