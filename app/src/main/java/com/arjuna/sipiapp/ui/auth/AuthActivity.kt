package com.arjuna.sipiapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arjuna.sipiapp.R
import com.arjuna.sipiapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authSectionsPagerAdapter = AuthSectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = authSectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        supportActionBar?.elevation = 0f
    }
}