package com.example.notesio.authentication.signUp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.notesio.R
import com.example.notesio.backend.viewmodel.AuthenticationViewModel
import com.example.notesio.backend.viewmodel.NoteViewModel
import com.example.notesio.databinding.FragmentSignUpScreenBinding
import com.example.notesio.utils.Constants
import com.example.notesio.utils.Constants.APP_ID
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType

class SignUpScreen : Fragment() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private var _binding: FragmentSignUpScreenBinding? = null
    private val authViewModel:AuthenticationViewModel by viewModels()
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
        val user = App.create(APP_ID).currentUser
        Log.d("Current User", (user == null).toString())
        oneTapClient = Identity.getSignInClient(context!!)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(Constants.Client_ID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
        val activityResultLauncher : ActivityResultLauncher<IntentSenderRequest> = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken
                    if(idToken != null) {
                        authViewModel.AppLogin(idToken, activity!!.getSharedPreferences("Notes_Auth", Context.MODE_PRIVATE))
                        val email = credential.id
                        Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signUp_to_newUser)
                    }
                } catch (e: ApiException) {
                    Log.d("Google Failed", e.message.toString())
                    // ...
                }
            }

        }
        binding.createAccount.setOnClickListener{
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener() { result ->
                    val intentSenderRequest : IntentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    activityResultLauncher.launch(intentSenderRequest)
                }
                .addOnFailureListener() { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d("Google Failed Inside", e.localizedMessage)
                }
        }
    }


}