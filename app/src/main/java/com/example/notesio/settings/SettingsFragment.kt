package com.example.notesio.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.notesio.MainActivity
import com.example.notesio.R
import com.example.notesio.backend.viewmodel.AuthenticationViewModel
import com.example.notesio.databinding.FragmentSettingsBinding
import com.example.notesio.utils.BackPressedListener


class SettingsFragment : Fragment(), BackPressedListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val authenticationViewModel : AuthenticationViewModel by viewModels()
    companion object {
        var backpressedlistener: BackPressedListener? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding
            .inflate(inflater, container, false)
        return binding.root

    }


    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide
            .with(this)
            .load("https://robohash.org/${MainActivity.auth.currentUser?.email.toString()}")
            .centerCrop()
            .placeholder(R.drawable.loading_gear)
            .into(binding.avatar);

        binding.profileName.text = MainActivity.auth.currentUser?.displayName

        binding.premiumButton.setOnClickListener {
            Toast.makeText(context, "You're already a premium user", Toast.LENGTH_SHORT).show()
        }

        binding.securityButton.setOnClickListener {
            Toast.makeText(context, "This feature is yet to be released", Toast.LENGTH_SHORT).show()
        }

        binding.editProfileButton.setOnClickListener{
            findNavController().navigate(R.id.action_settings_to_editProfile)
        }

        binding.themeButton.setOnClickListener{
            findNavController().navigate(R.id.action_settings_to_theme)
        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_settings_to_home)
        }

        binding.logoutButton.setOnClickListener {
            MainActivity.auth.signOut()
            findNavController().navigate(R.id.action_settings_to_signIn)
        }

    }

    override fun onPause() {
        super.onPause()
        backpressedlistener = null
    }

    override fun onResume() {
        super.onResume()
        backpressedlistener = this
    }

    override fun onBackPressed() {
        findNavController().navigate(R.id.action_settings_to_home)
    }


}