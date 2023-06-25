package com.example.notesio.authentication.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.notesio.R
import com.example.notesio.databinding.FragmentNewUserScreenBinding
import com.example.notesio.databinding.FragmentSignUpScreenBinding
import com.example.notesio.databinding.FragmentSubscriptionScreenBinding

class SignUpScreen : Fragment() {

    private var _binding: FragmentSignUpScreenBinding? = null

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
        binding.createAccount.setOnClickListener{
            findNavController().navigate(R.id.action_signUp_to_newUser)
        }
    }


}