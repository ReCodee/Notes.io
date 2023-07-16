package com.example.notesio.settings

import android.R.attr.checked
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notesio.R
import com.example.notesio.databinding.FragmentEditProfileBinding


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.passUpdate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                binding.updatePassVisibility.visibility = View.VISIBLE
            } else {
                binding.updatePassVisibility.visibility = View.INVISIBLE
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        }
    }
}