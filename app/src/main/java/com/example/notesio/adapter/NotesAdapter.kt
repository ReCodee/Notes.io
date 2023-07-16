package com.example.notesio.adapter

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.notesio.R
import com.example.notesio.backend.model.Note
import com.example.notesio.backend.model.NoteData
import com.example.notesio.editor.EditorScreenDirections
import com.example.notesio.gallery.GalleryScreenDirections
import org.w3c.dom.Text

class NotesAdapter(private val notes:List<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) {
            Log.d("Checking Before Displaying:", note.data.toString())
            itemView.findViewById<TextView>(R.id.title_text).setText(note.title)
            itemView.findViewById<TextView>(R.id.note_data).setText(note.data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notes_card, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bind(notes[position])
        holder.itemView.setOnClickListener{
          //val note : NoteData = NoteData(notes[position].email, notes[position].data, notes[position]._id.toString())
          val action = GalleryScreenDirections.actionHomeToEditor(notes[position])
            Log.d("Checking Before Passing", notes[position].data.toString())
          Navigation.findNavController(it).navigate(action)
        }
    }
}