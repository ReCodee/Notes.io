package com.example.notesio.backend.viewmodel
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MainActivity
import com.example.notesio.utils.Constants
import com.example.notesio.utils.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {
    fun AppLogin(token:String, sharedPreferences: SharedPreferences) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("Google_Auth_Token", token)
        editor.apply()
        viewModelScope.launch(Dispatchers.IO) {
            App.create(Constants.APP_ID).login(
                Credentials.google(token = token, type = GoogleAuthType.ID_TOKEN)
            ).loggedIn
        }
    }

    fun checkGoogleSession(token:String) : Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            App.create(Constants.APP_ID).login(
                Credentials.google(token = token, type = GoogleAuthType.ID_TOKEN)
            ).loggedIn
        }
        return App.create(APP_ID).currentUser != null
    }

}