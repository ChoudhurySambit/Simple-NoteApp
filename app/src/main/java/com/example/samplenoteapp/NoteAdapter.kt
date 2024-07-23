package com.example.samplenoteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.samplenoteapp.databinding.NotesitemsBinding


class NoteAdapter(val noteList:List<NoteItems>,private val itemClickListener:OnItemClickListener):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnItemClickListener{
        fun onClickDelete(nodeId:String)
        fun onClickUpdate(nodeId: String,noteTitle:String,noteDescription:String)
    }

    class NoteViewHolder(val binding:NotesitemsBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItems) {

            binding.notetitle.text=note.noteTitle
            binding.notedescription.text=note.noteDescription
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
       val binding=NotesitemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        setAnimation(holder.itemView)
        val note=noteList[position]
        holder.bind(note)
        holder.binding.deteteBtn.setOnClickListener {
            itemClickListener.onClickDelete(note.noteId)
        }
        holder.binding.updateBtn.setOnClickListener {
            itemClickListener.onClickUpdate(note.noteId,note.noteTitle,note.noteDescription)
        }
    }

    private fun setAnimation(itemView: View) {

        val animation=AlphaAnimation(0.0f,0.0f)
        animation.duration=1000
        itemView.startAnimation(animation)

    }
}