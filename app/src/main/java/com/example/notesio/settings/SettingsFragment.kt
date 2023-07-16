package com.example.notesio.settings

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.notesio.R
import com.example.notesio.databinding.FragmentEditorScreenBinding
import com.example.notesio.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide
            .with(this)
            .load("https://robohash.org/abhishek27082000@gmail.com")
            .centerCrop()
            .placeholder(R.drawable.sample_profile_pic)
            .into(binding.avatar);

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

    }

}