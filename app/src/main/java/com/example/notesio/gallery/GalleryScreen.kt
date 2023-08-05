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
import com.example.notesio.MainActivity
import com.example.notesio.R
import com.example.notesio.adapter.NotesAdapter
import com.example.notesio.backend.model.Note
import com.example.notesio.backend.model.NoteData
import com.example.notesio.backend.repository.MongoRepositoryImpl
import com.example.notesio.backend.viewmodel.NoteViewModel
import com.example.notesio.databinding.FragmentGalleryScreenBinding
import com.example.notesio.editor.EditorScreenDirections
import com.example.notesio.settings.SettingsFragment
import com.example.notesio.utils.BackPressedListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryScreen : Fragment(), BackPressedListener {

    private var _binding: FragmentGalleryScreenBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel : NoteViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    companion object {
        var backpressedlistener: BackPressedListener? = null
    }
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
        val dbReference = FirebaseDatabase.getInstance().getReference("Notes")
        dbReference.keepSynced(true)
        recyclerView = binding.recyclerView

        recyclerView.layoutManager = GridLayoutManager(context, 2)

        noteViewModel.data.observe(this) {
            //noteViewModel.filterNotes(MongoRepositoryImpl.sharedPreferences?.getString("Email","").toString())
            recyclerView.adapter = NotesAdapter(it)
            binding.shimmerLayout.visibility = View.GONE
            //Toast.makeText(context, it.size.toString(), Toast.LENGTH_SHORT).show()
        }
        /*val data : MutableList<Note> = emptyList<Note>().toMutableList()
        MainActivity.dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (noteSnap in snapshot.children){
                        val noteData = noteSnap.getValue(Note::class.java)
                        data.add(noteData!!)
                    }
              recyclerView.adapter = NotesAdapter(data)
                    Toast.makeText(context, data.size.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/


        binding.navigationDrawer.setOnClickListener{
            findNavController().navigate(R.id.action_home_to_settings)
        }

        binding.addButton.setOnClickListener{
            val action = GalleryScreenDirections.actionHomeToEditor(Note())
            findNavController().navigate(action)
        }

        binding.Search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val noteList = noteViewModel.data.value;
                 val mutableList  = noteList?.filter {
                     it.data.contains(query.toString()) || it.title.contains(query.toString())
                 }?.toMutableList()
                noteViewModel.data.value = mutableList
                Log.d("Submit", query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                noteViewModel.getNoteData {
                    val noteList = noteViewModel.data.value;
                    val mutableList  = noteList?.filter {
                        it.data.contains(newText.toString()) || it.title.contains(newText.toString())
                    }?.toMutableList()
                    noteViewModel.data.value = mutableList
                }
                if ( newText?.length == 0 ) {
                    noteViewModel.getNoteData {

                    }
                }
                 Log.d("Change", newText.toString())
                return false
            }
        })
    }
    override fun onPause() {
        binding.shimmerLayout.stopShimmer()
        super.onPause()
        backpressedlistener = null
    }

    override fun onResume() {
        binding.shimmerLayout.startShimmer()
        super.onResume()
        backpressedlistener = this
    }

    override fun onBackPressed() {

    }

}
