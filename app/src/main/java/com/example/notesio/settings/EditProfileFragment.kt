package com.example.notesio.settings

import android.R.attr.checked
import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notesio.MainActivity
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

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileUpdateEmail.setText(MainActivity.auth.currentUser?.email)
        binding.profileUpdateFullname.setText(MainActivity.auth.currentUser?.displayName)

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
        binding.updateSaveButton.setOnClickListener {
            //Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show()
            if ( binding.passUpdate.isChecked ) {
                if (binding.oldPass.text == binding.newPass.text)
                    Toast.makeText(context, "Clicked1", Toast.LENGTH_LONG).show()
                    MainActivity.auth.currentUser?.updatePassword(binding.newPass.text.toString())
                        ?.addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(context, "Change Successful", Toast.LENGTH_LONG).show()
                            }
                        }?.addOnFailureListener {
                            Toast.makeText(context, "Please login again", Toast.LENGTH_LONG).show()
                            Log.d("Pass Change", it.message.toString())
                            MainActivity.auth.signOut()
                            findNavController().navigate(R.id.action_profile_to_signIn)
                        }
            }
            if ( MainActivity.auth.currentUser?.email != binding.profileUpdateEmail.text.toString() )
            MainActivity.auth.currentUser?.updateEmail(binding.profileUpdateEmail.text.toString())
                ?.addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(context, "Change Successful", Toast.LENGTH_LONG).show()
                    }
                }?.addOnFailureListener {
                    Toast.makeText(context, "Please login again", Toast.LENGTH_LONG).show()
                    Log.d("Email Change", it.message.toString())
                    MainActivity.auth.signOut()
                    findNavController().navigate(R.id.action_profile_to_signIn)
                }
        }
    }
}