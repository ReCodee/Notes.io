package com.example.notesio.authentication.signUp

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notesio.MainActivity
import com.example.notesio.R
import com.example.notesio.backend.repository.MongoRepositoryImpl
import com.example.notesio.backend.viewmodel.AuthenticationViewModel
import com.example.notesio.backend.viewmodel.NoteViewModel
import com.example.notesio.databinding.FragmentSignUpScreenBinding
import com.example.notesio.utils.Constants
import com.example.notesio.utils.Constants.APP_ID
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import io.realm.kotlin.mongodb.App

class SignUpScreen : Fragment() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private var _binding: FragmentSignUpScreenBinding? = null
    private val authViewModel:AuthenticationViewModel by viewModels()
    private val noteViewModel:NoteViewModel by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences:SharedPreferences = activity!!.getSharedPreferences("Notes_Auth", Context.MODE_PRIVATE)
        val user = App.create(APP_ID).currentUser
        Log.d("Current User", (user == null).toString())
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.Client_ID)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        binding.GoogleButton.setOnClickListener{
            googleSignInClient.signOut()
            startActivityForResult(googleSignInClient.signInIntent, 13)
        }
        binding.createAccount.setOnClickListener{
            val email : String = binding.email.text.toString()
            val pass : String = binding.pass.text.toString()
            if(email.isNotEmpty() && pass.isNotEmpty())
                MainActivity.auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                    if(it.isSuccessful){
                        val userProfile : UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(binding.fullName.text.toString()).build()
                        MainActivity.auth.currentUser?.updateProfile(userProfile)
                        findNavController().navigate(R.id.action_signUp_to_newUser)
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
        binding.toLogin.setOnClickListener{
            findNavController().navigate(R.id.action_signUp_to_signIn)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 13 && resultCode == RESULT_OK) {
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
                            findNavController().navigate(R.id.action_signUp_to_home)
                        else
                            findNavController().navigate(R.id.action_signUp_to_newUser)
                    })
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }


}