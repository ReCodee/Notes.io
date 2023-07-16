package com.example.notesio.otherScreens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.notesio.R
import com.example.notesio.backend.viewmodel.AuthenticationViewModel
import com.example.notesio.databinding.FragmentWelcomeScreenBinding
import com.onegravity.rteditor.api.RTApi.getApplicationContext

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {

            sPreferences = activity!!.getSharedPreferences("Notes_Auth", Context.MODE_PRIVATE)
            val googleAuthToken = sPreferences.getString("Google_Auth_Token", "")

            if (googleAuthToken != null &&  googleAuthToken != "") {
                if (authenticationViewModel.checkGoogleSession(googleAuthToken)) {
                    findNavController().navigate(R.id.action_welcome_to_new_user)
                }
            }
        } catch (ex : Exception) {
            Log.d("Auth Session Fail", ex.message.toString())
        }


        binding.GetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_subscription)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}