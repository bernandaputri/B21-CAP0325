package com.arjuna.sipiapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjuna.sipiapp.MainViewModel
import com.arjuna.sipiapp.R
import com.arjuna.sipiapp.data.ReportEntity
import com.arjuna.sipiapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        homeAdapter = HomeAdapter()

        with(binding) {
            reportList.layoutManager = LinearLayoutManager(context)
            reportList.adapter = homeAdapter
            reportList.setHasFixedSize(true)
            reportList.apply {
                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            }
        }

        mainViewModel.getAllReport(binding).observe(viewLifecycleOwner, {
            if (it != null) {
                homeAdapter.setReports(it)
                binding.homeProgressBar.visibility = View.GONE
            }
        })

        return binding.root
    }

}