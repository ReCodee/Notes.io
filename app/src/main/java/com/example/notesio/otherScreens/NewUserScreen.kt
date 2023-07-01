package com.example.notesio.otherScreens

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notesio.R
import com.example.notesio.databinding.FragmentNewUserScreenBinding
import com.example.notesio.databinding.FragmentSignUpScreenBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewUserScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewUserScreen : Fragment() {

    private var _binding: FragmentNewUserScreenBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activity.setActionBar()

        /*activity?.actionBar?.displayOptions = android.app.ActionBar.DISPLAY_SHOW_CUSTOM;
        activity?.actionBar?.setDisplayShowCustomEnabled(true);
        activity?.actionBar?.setCustomView(R.layout.custom_action_bar_layout);*/


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewUserScreenBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newUserButton.setOnClickListener{
            findNavController().navigate(R.id.action_newUser_to_gallery)
        }
    }


}

