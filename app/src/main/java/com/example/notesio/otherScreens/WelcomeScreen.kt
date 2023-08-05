package com.example.notesio.otherScreens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.notesio.MainActivity
import com.example.notesio.R
import com.example.notesio.backend.repository.MongoRepositoryImpl
import com.example.notesio.backend.viewmodel.AuthenticationViewModel
import com.example.notesio.backend.viewmodel.NoteViewModel
import com.example.notesio.databinding.FragmentWelcomeScreenBinding
import com.example.notesio.utils.Constants
import com.google.firebase.database.FirebaseDatabase
import io.realm.kotlin.mongodb.App

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class WelcomeScreen : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentWelcomeScreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sPreferences : SharedPreferences
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sPreferences = activity!!.getSharedPreferences("Notes_Auth", Context.MODE_PRIVATE)
        val googleAuthToken = sPreferences.getString("Google_Auth_Token", "")
        val email = sPreferences.getString("Email", "")
        val pass = sPreferences.getString("Pass", "")
        Log.d("Inside Welcome Screen", googleAuthToken.toString())
        Log.d("Inside Welcome Screen", email + " " + pass)
        /*val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences("Notes_Auth", Context.MODE_PRIVATE)
        val user = App.create(Constants.APP_ID).currentUser
        Log.d("Checking User" , user.toString())
        Toast.makeText(context, user.toString(), Toast.LENGTH_SHORT).show()

        val google = sharedPreferences.getString("Google_Auth_Token", "")
        val email = sharedPreferences.getString("Email", "")
        val pass = sharedPreferences.getString("Pass", "")

        if (google != null && google != "") {
            Log.d("Checking Google" , google.toString())
            findNavController().navigate(R.id.action_welcome_to_new_user)
        } else if ( email != null && email != "" && pass != null && pass != "" ) {
            Log.d("Checking Email Pass" , "$email, $pass")
            findNavController().navigate(R.id.action_welcome_to_new_user)
        }*/
        _binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ( MainActivity.auth.currentUser != null ) {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressBar.bringToFront()
            binding.GetStarted.isEnabled = false
            binding.GetStarted.isClickable = false
            noteViewModel.getOneTimeNoteData {
                if ( noteViewModel.data.value?.isNotEmpty() == true ) {
                    binding.progressBar.visibility = View.GONE
                    binding.GetStarted.isEnabled = true
                    binding.GetStarted.isClickable = true
                    findNavController().navigate(R.id.action_welcome_to_home)
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.GetStarted.isEnabled = true
                    binding.GetStarted.isClickable = true
                    findNavController().navigate(R.id.action_welcome_to_new_user)
                }
            }
        }

        binding.alreadyAccount.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_signIn)
        }


        binding.GetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_signUp)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}