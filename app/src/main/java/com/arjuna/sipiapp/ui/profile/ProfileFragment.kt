package com.arjuna.sipiapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjuna.sipiapp.MainViewModel
import com.arjuna.sipiapp.R
import com.arjuna.sipiapp.UserPreferences
import com.arjuna.sipiapp.data.ReportEntity
import com.arjuna.sipiapp.databinding.FragmentProfileBinding
import com.arjuna.sipiapp.ui.auth.AuthActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userPref: UserPreferences
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        userPref = UserPreferences(requireActivity())

        val userModel = userPref.getUser()
        username = userModel.username!!
        val name = userModel.name!!

        binding.collapseToolbar.title = name
        binding.btnLogout.setOnClickListener {
            userPref.removeUser()
            val logoutIntent = Intent(activity, AuthActivity::class.java)
            startActivity(logoutIntent)
            activity?.finish()
        }

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        profileAdapter = ProfileAdapter()

        with(binding) {
            myReportList.layoutManager = LinearLayoutManager(context)
            myReportList.adapter = profileAdapter
            myReportList.setHasFixedSize(true)
            myReportList.apply {
                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            }
        }

        mainViewModel.getUserReport(username, binding).observe(viewLifecycleOwner, {
            if (it != null) {
                profileAdapter.setReports(it)
                binding.progressBar.visibility = View.GONE
            }
        })

        return binding.root
    }

}