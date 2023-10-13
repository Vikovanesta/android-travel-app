package com.example.uts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import com.example.uts.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        with(binding) {

            // Get extra data from register activity
            val correctUsername = intent.getStringExtra(RegisterActivity.EXTRA_USERNAME)
            val correctEmail = intent.getStringExtra(RegisterActivity.EXTRA_EMAIL)
            val correctPassword = intent.getStringExtra(RegisterActivity.EXTRA_PASSWORD)

            // Go to home activity
            btnLogin.setOnClickListener {
                val intentToHomeActivity =
                    android.content.Intent(
                        this@LoginActivity,
                        HomeActivity::class.java
                    )

                // Validation
                var validated = true
                val username = editTextUsername.text.toString()
                val password = editTextPassword.text.toString()
                val email = editTextEmail.text.toString()

                if (username.isEmpty()) {
                    editTextUsername.error = "Username is required"
                    validated = false
                }
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
                if (username != correctUsername ||
                    email != correctEmail ||
                    password != correctPassword) {
                    editTextUsername.error = "User not found"
                    editTextEmail.error = "User not found"
                    editTextPassword.error = "User not found"
                    validated = false
                }

                if (validated) {
                    startActivity(intentToHomeActivity)
                }
            }

            //Clickable span to redirect to register activity
            val spannableString = android.text.SpannableString(textViewRedirectRegister.text)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: android.view.View) {
                    val intentToMainActivity =
                        android.content.Intent(
                            this@LoginActivity,
                            RegisterActivity::class.java
                        )
                    startActivity(intentToMainActivity)
                }
            }

            spannableString.setSpan(clickableSpan, 12, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textViewRedirectRegister.text = spannableString
            textViewRedirectRegister.movementMethod = LinkMovementMethod.getInstance()
        }

        setContentView(binding.root)
    }
}