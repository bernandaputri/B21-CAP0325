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
import com.arjuna.sipiapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val username = binding.edtUsername.text.toString().trim()
        val name = binding.edtName.text.toString()

        if (email.isEmpty()) {
            binding.edtEmail.error = getString(R.string.email_required)
        }

        if (password.isEmpty()) {
            binding.edtPassword.error = getString(R.string.password_required)
        }

        if (name.isEmpty()) {
            binding.edtName.error = getString(R.string.name_required)
        }

        if (username.isEmpty()) {
            binding.edtUsername.error = getString(R.string.username_required)
        }

        if (!isValidEmail(email)) {
            binding.edtEmail.error = getString(R.string.email_invalid)
        }

        saveUser(email, password, name, username)
    }

    private fun saveUser(email: String, password: String, name: String, username: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(activity, "Akun berhasil dibuat.", Toast.LENGTH_SHORT).show()
            userId = firebaseAuth.currentUser!!.uid
            val documentReference = firebaseFirestore.collection("users").document(userId)
            val user = HashMap<String, Any>()
            user["email"] = email
            user["password"] = password
            user["fullname"] = name
            user["username"] = username
            documentReference.set(user)
            val registerIntent = Intent(activity, MainActivity::class.java)
            startActivity(registerIntent)
        }.addOnFailureListener {
            Toast.makeText(activity, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidEmail(email: CharSequence) : Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}