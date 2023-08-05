package com.example.notesio.backend.viewmodel
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var authenticated = MutableLiveData<Boolean>(false)


fun AppLogin(tokenId:String, email:String, sharedPreferences: SharedPreferences,  onSuccess: () -> Unit,
                 onError: (Exception) -> Unit) {
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putString("Google_Auth_Token", tokenId)
    editor.putString("Email", email)
    editor.apply()
    viewModelScope.launch {
        try {
            val result = withContext(Dispatchers.IO) {
                App.create(APP_ID).login(
                    Credentials.google(token = tokenId, type = GoogleAuthType.ID_TOKEN)
                ).loggedIn
            }
            withContext(Dispatchers.Main) {
                if (result) {
                    onSuccess()
                    delay(600)
                    authenticated.value = true
                } else {
                    onError(Exception("User is not logged in."))
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError(e)
            }
        }
    }
 }
    fun VerifyGoogleSession(tokenId:String, onSuccess: () -> Unit,
                 onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID).login(
                        Credentials.google(token = tokenId, type = GoogleAuthType.ID_TOKEN)
                    ).loggedIn
                }
                withContext(Dispatchers.Main) {
                    if (result) {
                        onSuccess()
                        delay(600)
                        authenticated.value = true
                    } else {
                        onError(Exception("User is not logged in."))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    fun EmailPassAppRegister(email:String, pass:String, sharedPreferences: SharedPreferences, onSuccess: () -> Unit,
                             onError: (Exception) -> Unit) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("Pass", pass)
        editor.putString("Email", email)
        editor.apply()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
/*                    App.create(APP_ID).login(
                        Credentials.emailPassword(email, pass)
                    ).loggedIn*/
                    val app = App.create(APP_ID)
                    val user = app.login(Credentials.anonymous()) // logs in with an anonymous user
                    // registers an email/password user
                    app.emailPasswordAuth.registerUser(email, pass)
                    // links anonymous user with email/password credentials
                    user.linkCredentials(Credentials.emailPassword(email, pass));
                }
                withContext(Dispatchers.Main) {
                    if (result.loggedIn) {
                        onSuccess()
                        delay(600)
                        authenticated.value = true
                    } else {
                        onError(Exception("Unable to create account"))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    fun EmailPassAppLogin(email:String, pass:String, sharedPreferences: SharedPreferences, onSuccess: () -> Unit,
                             onError: (Exception) -> Unit) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.putString("Pass", pass)
        editor.putString("Email", email)
        editor.apply()
        val emaill = sharedPreferences.getString("Email", "")!!
        val passs = sharedPreferences.getString("Pass", "")!!
        Log.d("Check in AuthViewModel", emaill)
        Log.d("Check in AuthViewModel", passs)
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID).login(
                        Credentials.emailPassword(email, pass)
                    ).loggedIn
                }
                withContext(Dispatchers.Main) {
                    if (result) {
                        val app = App.create(APP_ID)
                        val allUsers = app.allUsers()
                        for ((key, value) in allUsers.entries) {
                            if ( key != app.currentUser?.id )
                            value.linkCredentials(Credentials.emailPassword(email, pass))
                        }
                        onSuccess()
                        delay(600)
                        authenticated.value = true
                    } else {
                        onError(Exception("Unable to create account"))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)

                }
            }
        }
    }

    fun AppLogOut() {
        viewModelScope.launch {
            App.create(Constants.APP_ID).currentUser?.logOut()
        }
    }

}