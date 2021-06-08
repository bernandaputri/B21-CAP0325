package com.arjuna.sipiapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arjuna.sipiapp.MainActivity
import com.arjuna.sipiapp.R
import com.arjuna.sipiapp.UserPreferences
import com.arjuna.sipiapp.data.UserModel
import com.arjuna.sipiapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var userPref: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        userPref = UserPreferences(requireContext())

        binding.btnLogin.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (password.isEmpty()) {
            binding.edtPassword.error = getString(R.string.password_required)
        }

        if (email.isEmpty()) {
            binding.edtEmail.error = getString(R.string.username_required)
        }

        if (!isValidEmail(email)) {
            binding.edtEmail.error = getString(R.string.email_invalid)
        }

        if (email.isNotEmpty() && password.isNotEmpty()) {
            processUser(email, password)
        }

    }

    private fun processUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(activity, "Selamat Datang!", Toast.LENGTH_SHORT).show()
            firebaseFirestore.collection("users").whereEqualTo("email", email).get().addOnSuccessListener { it ->
                it.forEach {
                    if (email == it.data["email"] && password == it.data["password"]) {
                        val username = it.data["username"].toString()
                        val name = it.data["fullname"].toString()
                        val userModel = UserModel(email, username, name)
                        userPref.setUser(userModel)
                    } else {
                        Toast.makeText(activity, "Email/Password tidak sesuai.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            val loginIntent = Intent(activity, MainActivity::class.java)
            startActivity(loginIntent)
            activity?.finish()
        }
    }

    private fun isValidEmail(email: CharSequence) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}