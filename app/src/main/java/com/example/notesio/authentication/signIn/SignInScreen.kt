package com.example.notesio.authentication.signIn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notesio.MainActivity
import com.example.notesio.R
import com.example.notesio.backend.repository.MongoRepositoryImpl
import com.example.notesio.backend.viewmodel.AuthenticationViewModel
import com.example.notesio.backend.viewmodel.NoteViewModel
import com.example.notesio.databinding.FragmentSignInScreenBinding
import com.example.notesio.utils.Constants
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import io.realm.kotlin.mongodb.App


class SignInScreen : Fragment() {

    private var _binding: FragmentSignInScreenBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel : NoteViewModel by viewModels()
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInScreenBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences("Notes_Auth", Context.MODE_PRIVATE)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.Client_ID)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity!!, gso)

        binding.GoogleButton.setOnClickListener{
            googleSignInClient.signOut()
            startActivityForResult(googleSignInClient.signInIntent, 13)
        }
        binding.loginButton.setOnClickListener{
            val email : String = binding.email.text.toString()
            val pass : String = binding.pass.text.toString()
            if(email.isNotEmpty() && pass.isNotEmpty())
                MainActivity.auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if(it.isSuccessful){
                        noteViewModel.getOneTimeNoteData(onSuccess = {
                            if (noteViewModel.data.value?.isNotEmpty() == true)
                                findNavController().navigate(R.id.action_signIn_to_home)
                            else
                                findNavController().navigate(R.id.action_signIn_to_newUser)
                        })
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }

        binding.toSignup.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 13 && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        MainActivity.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    noteViewModel.getOneTimeNoteData(onSuccess = {
                        if (noteViewModel.data.value?.isNotEmpty() == true)
                            findNavController().navigate(R.id.action_signIn_to_home)
                        else
                            findNavController().navigate(R.id.action_signIn_to_newUser)
                    })
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }


}