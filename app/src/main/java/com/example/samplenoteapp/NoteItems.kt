package com.example.samplenoteapp

data class NoteItems(val noteTitle:String,val noteDescription:String,val noteId:String){
    constructor():this("","","")
}
