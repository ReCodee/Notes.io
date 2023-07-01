package com.example.notesio.gallery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesio.R
import com.example.notesio.adapter.NotesAdapter
import com.example.notesio.backend.model.Note
import com.example.notesio.backend.viewmodel.NoteViewModel
import com.example.notesio.databinding.FragmentGalleryScreenBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryScreen : Fragment() {

    private var _binding: FragmentGalleryScreenBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel : NoteViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryScreenBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        noteViewModel.data.observe(this) {
            recyclerView.adapter = NotesAdapter(it)
            Toast.makeText(context, it.size.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.Search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val note = Note()
                note.email = "abhishek27082000@gmail.com"
                note.data = query.toString()
                val notes : MutableList<Note>? = noteViewModel.data.value?.toMutableList()
                notes?.add(note)
                noteViewModel.data.value = notes
                noteViewModel.insertNote(note)
                Log.d("Submit", query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                 Log.d("Change", newText.toString())
                return false
            }
        })

    }

}
