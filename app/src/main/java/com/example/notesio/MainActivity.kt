package com.example.notesio

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.notesio.R
import com.example.notesio.backend.model.Note
import com.example.notesio.backend.repository.MongoRepositoryImpl
import com.example.notesio.backend.viewmodel.AuthenticationViewModel
import com.example.notesio.backend.viewmodel.NoteViewModel
import com.example.notesio.databinding.ActivityMainBinding
import com.example.notesio.gallery.GalleryScreen
import com.example.notesio.otherScreens.NewUserScreen
import com.example.notesio.settings.SettingsFragment
import com.example.notesio.utils.BackPressedListener
import com.example.notesio.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONArray


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var sPreferences : SharedPreferences
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val noteViewModel : NoteViewModel by viewModels()

    companion object {
        lateinit var auth : FirebaseAuth
        lateinit var dbRef : DatabaseReference
    }

    /*private lateinit var email : String
    private lateinit var pass : String
    private lateinit var googleAuthToken : String*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Notes")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.skin)));

        /*sPreferences = this.getSharedPreferences("Notes_Auth", Context.MODE_PRIVATE)
        googleAuthToken = sPreferences.getString("Google_Auth_Token", "")!!
        email = sPreferences.getString("Email", "")!!
        pass = sPreferences.getString("Pass", "")!!
        Toast.makeText(this, "Inside onStart", Toast.LENGTH_SHORT).show()
        Log.d("MainActivity in Activity", googleAuthToken)
        Log.d("MainActivity in Activity", email + " " + pass)

        if (googleAuthToken != "") {
            authenticationViewModel.VerifyGoogleSession(googleAuthToken, onSuccess = {
                Log.d("OnSuccess", "Executed")
                MongoRepositoryImpl.sharedPreferences = sPreferences
                val intent = Intent(this, NewUserScreen::class.java)
                startActivity(intent)
            }, onError = {
                Log.d("OnError", "Executed")
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                val editor: SharedPreferences.Editor = sPreferences.edit()
                editor.putString("Google_Auth_Token", "")
                editor.apply()
            })
        } else if (email != "" && pass != "") {
            authenticationViewModel.EmailPassAppLogin(email, pass, sPreferences, onSuccess = {
                MongoRepositoryImpl.sharedPreferences = sPreferences
                Log.d("MainActivity Auth Check", "Success")
                findNavController(R.id.NewUserFragment).navigate(R.id.action_activity_to_newUser)
            }, onError = {
                Log.d("MainActivity Auth Check", it.message.toString())
                val editor: SharedPreferences.Editor = sPreferences.edit()
                editor.clear()
                editor.apply()
            })
        }*/
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {
        if ( SettingsFragment.backpressedlistener != null )
        SettingsFragment.backpressedlistener?.onBackPressed()
        else if (GalleryScreen.backpressedlistener != null ) {

        } else {
            super.onBackPressed()
        }
    }

}