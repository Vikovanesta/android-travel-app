package com.example.uts.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.uts.R
import com.example.uts.databinding.FragmentLoginBinding
import com.example.uts.model.User
import com.example.uts.ui.MainActivity
import com.example.uts.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val pager = activity?.findViewById<ViewPager2>(R.id.view_pager)

        sessionManager = SessionManager.getInstance(requireContext())

        if (sessionManager.isLoggedIn()) {
            goToMainActivity()
        }

        with(binding) {

            btnLogin.setOnClickListener {
                // Validation
                var validated = true
                val password = editTextPassword.text.toString()
                val email = editTextEmail.text.toString()

                if (password.isEmpty()) {
                    editTextPassword.error = "Password is required"
                    validated = false
                }
                if (email.isEmpty()) {
                    editTextEmail.error = "Email is required"
                    validated = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.error = "Invalid email format"
                    validated = false
                }

                if (validated) {
                    login(email, password)
                }
            }

            //Clickable span to redirect to register fragment
            val spannableString = android.text.SpannableString(textViewRedirectRegister.text)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    if (pager != null) {
                        pager.currentItem = 0
                    }
                }
            }

            spannableString.setSpan(clickableSpan, 12, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textViewRedirectRegister.text = spannableString
            textViewRedirectRegister.movementMethod = LinkMovementMethod.getInstance()
        }

        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser

                    // Grab user data from firestore & save to shared preferences
                    currentUser?.let { it ->
                        val uid = it.uid
                        usersCollection.document(uid).get()
                            .addOnSuccessListener { document ->
                                val user = document.toObject(User::class.java)
                                user?.let {
                                    saveUserToSharedPreferences(it)
                                }
                            }
                    }

                    Log.d("Login", "Login success")
                    Toast.makeText(requireContext(), "Login success", Toast.LENGTH_SHORT).show()

                    goToMainActivity()
                } else {
                    Log.d("Login", "Login failed")
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun saveUserToSharedPreferences(user: User) {
        sessionManager.setLoggedIn(true)
        sessionManager.setUserId(user.id)
        sessionManager.setUserRole(user.role)
        sessionManager.setUsername(user.username)
        sessionManager.setUserEmail(user.email)
        sessionManager.setUserNim(user.nim)
    }
}