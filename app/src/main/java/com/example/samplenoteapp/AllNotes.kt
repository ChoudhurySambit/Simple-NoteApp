package com.example.samplenoteapp

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samplenoteapp.databinding.ActivityAllNotesBinding
import com.example.samplenoteapp.databinding.AlertdialongupdateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shashank.sony.fancytoastlib.FancyToast

class AllNotes : AppCompatActivity(),NoteAdapter.OnItemClickListener {

    private val binding:ActivityAllNotesBinding by lazy{
        ActivityAllNotesBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference

        val recyclerView=binding.recyclerView
        recyclerView.layoutManager=LinearLayoutManager(this)


        val currentUser=auth.currentUser

        currentUser?.let { user->
            val noteReference=databaseReference.child("Users").child(user.uid).child("Notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteLists= mutableListOf<NoteItems>()
                    for(noteSnapshot in snapshot.children){

                        var note=noteSnapshot.getValue(NoteItems::class.java)
                        note?.let {
                            noteLists.add(note)
                        }

                    }

                    val adapter=NoteAdapter(noteLists.reversed(),this@AllNotes)
                    recyclerView.adapter=adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    override fun onClickDelete(nodeId: String) {

        val currentUser=auth.currentUser

        currentUser?.let {user->

            val notereference=databaseReference.child("Users").child(user.uid).child("Notes")
            notereference.child(nodeId).removeValue()
            FancyToast.makeText(this,"Successfully Deleted The Note ",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()


        }
    }

    override fun onClickUpdate(nodeId: String,noteTitle:String,noteDescription:String) {

        val viewDialog=AlertdialongupdateBinding.inflate(LayoutInflater.from(this))

        val dialog=AlertDialog.Builder(this).setView(viewDialog.root)
            .setTitle("Update Note")
            .setPositiveButton("Update"){ dialog,_->
                val newTitle=viewDialog.updatedTitle.text.toString()
                val newDescription=viewDialog.updatedDescription.text.toString()
                updateNoteToDataBase(nodeId,newTitle,newDescription)
                dialog.dismiss()
        }
            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .create()
        viewDialog.updatedTitle.setText(noteTitle)
        viewDialog.updatedDescription.setText(noteDescription)
        dialog.show()
    }

    private fun updateNoteToDataBase(nodeId: String, newTitle: String, newDescription: String) {
        val currentUser=auth.currentUser
        currentUser?.let { user->
            val notereference=databaseReference.child("Users").child(user.uid).child("Notes")
            val noteListItems=NoteItems(newTitle,newDescription,nodeId)
            notereference.child(nodeId).setValue(noteListItems).addOnCompleteListener {
                if(it.isSuccessful){
                    FancyToast.makeText(this,"Successfully Value Updated",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true)
                }
                else{
                    FancyToast.makeText(this,"Failed To Update The Value",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true)
                }
            }

        }
    }
}