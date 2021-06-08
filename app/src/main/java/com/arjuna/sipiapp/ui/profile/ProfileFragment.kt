package com.arjuna.sipiapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.arjuna.sipiapp.R
import com.arjuna.sipiapp.UserPreferences
import com.arjuna.sipiapp.databinding.FragmentProfileBinding
import com.arjuna.sipiapp.ui.auth.AuthActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userPref: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().actionBar?.hide()
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPreferences(requireActivity())

        val userModel = userPref.getUser()
        val name = userModel.name

        binding.collapseToolbar.title = name
        binding.btnLogout.setOnClickListener {
            userPref.removeUser()
            val logoutIntent = Intent(activity, AuthActivity::class.java)
            startActivity(logoutIntent)
            activity?.finish()
        }
    }

}