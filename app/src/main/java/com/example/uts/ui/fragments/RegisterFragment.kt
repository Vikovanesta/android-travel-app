package com.example.uts.ui.fragments

import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.uts.R
import com.example.uts.databinding.FragmentRegisterBinding
import com.example.uts.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val usersCollection = firestore.collection("users")
    private val sharedPreferences = activity?.getSharedPreferences("Auth", android.content.Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val pager = activity?.findViewById<ViewPager2>(R.id.view_pager)

        with(binding) {

            btnRegister.setOnClickListener {
                val user = User(
                    "",
                    editTextUsername.text.toString(),
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString(),
                    editTextNim.text.toString(),
                    "user"
                )

                // Validation
                var validated = true

                if (user.username.isEmpty()) {
                    editTextUsername.error = "Username is required"
                    validated = false
                }

                if (user.email.isEmpty()) {
                    editTextEmail.error = "Email is required"
                    validated = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user.email).matches()){
                    editTextEmail.error = "Invalid email format"
                    validated = false
                }

                if (user.password.isEmpty()) {
                    editTextPassword.error = "Password is required"
                    validated = false
                } else if (user.password.length < 8) {
                    editTextPassword.error = "Password must be at least 8 characters long"
                    validated = false
                }

                if (validated) {
                    registerUser(user)
                }
            }

            // Clickable span
            val spannableString = android.text.SpannableString(textViewRedirectLogin.text)
            val clickableSpan = object : android.text.style.ClickableSpan() {
                override fun onClick(widget: View) {
                    if (pager != null) {
                        pager.currentItem = 1
                    }
                }
            }

            spannableString.setSpan(clickableSpan, 25, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textViewRedirectLogin.text = spannableString
            textViewRedirectLogin.movementMethod = LinkMovementMethod.getInstance()
        }

        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun registerUser(user: User) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser

                    // Add user data to Firestore
                    if (currentUser != null) {
                        val uid = currentUser.uid
                        val newUser = User(
                            uid,
                            user.username,
                            user.email,
                            nim = user.nim,
                            role = "user"
                        )

                        usersCollection.document(uid).set(newUser)
                            .addOnSuccessListener {
                                saveUserToSharedPreferences(newUser)

                                Log.d("RegisterFragment", "User successfully registered!")
                                Toast.makeText(requireContext(), "User successfully registered!", Toast.LENGTH_SHORT).show()

                                goToMainActivity()
                            }
                            .addOnFailureListener {
                                Log.e("RegisterFragment", "Error adding user: ", it)
                                Toast.makeText(requireContext(), "Error adding user!", Toast.LENGTH_SHORT).show()
                            }
                    }
                    else {
                        Log.e("RegisterFragment", "Error adding user: ", task.exception)
                        Toast.makeText(requireContext(), "Error adding user!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Log.e("RegisterFragment", "Email already in use: ", task.exception)
                        Toast.makeText(requireContext(), "Email is already in use", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("RegisterFragment", "Error adding user: ", task.exception)
                        Toast.makeText(requireContext(), "Error adding user!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun saveUserToSharedPreferences(user: User) {
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor.putBoolean("isLoggedIn", true)
            editor.putString("userRole", user.role)
            editor.putString("username", user.username)
            editor.putString("userEmail", user.email)
            editor.putString("userNim", user.nim)
            editor.apply()
        }
    }

    private fun goToMainActivity() {
        // TODO: Go to main activity
        Toast.makeText(requireContext(), "Go to main activity", Toast.LENGTH_SHORT).show()
    }
}