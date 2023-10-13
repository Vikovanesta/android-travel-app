package com.example.uts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import com.example.uts.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_BIRTHDATE = "extra_birthdate"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        with(binding) {
            // Go to second activity
            btnRegister.setOnClickListener {
                val intentToHomeActivity =
                    Intent(
                        this@RegisterActivity,
                        LoginActivity::class.java
                    )
                // Pass data to second activity
                val username = editTextUsername.text.toString()
                val email = editTextEmail.text.toString()
                val birthdate = editTextBirthdate.text.toString()
                val password = editTextPassword.text.toString()

                intentToHomeActivity.putExtra(EXTRA_USERNAME, username)
                intentToHomeActivity.putExtra(EXTRA_EMAIL, email)
                intentToHomeActivity.putExtra(EXTRA_BIRTHDATE, birthdate)

                if (username.isNotEmpty() && email.isNotEmpty() && birthdate.isNotEmpty() && password.isNotEmpty() && checkBox.isChecked) {
                    startActivity(intentToHomeActivity)
                }
                if (username.isEmpty()) {
                    editTextUsername.error = "Username tidak boleh kosong"
                }
                if (email.isEmpty()) {
                    editTextEmail.error = "Email tidak boleh kosong"
                }
                if (birthdate.isEmpty()) {
                    editTextBirthdate.error = "Tanggal lahir tidak boleh kosong"
                }
                if (password.isEmpty()) {
                    editTextPassword.error = "Password tidak boleh kosong"
                }
                if (!checkBox.isChecked) {
                    checkBox.error = "Anda harus menyetujui syarat dan ketentuan"
                }


            }

            // Clickable span
            val spannableString = android.text.SpannableString(textViewRedirectLogin.text)
            val clickableSpan = object : android.text.style.ClickableSpan() {
                override fun onClick(widget: android.view.View) {
                    val intentToLoginActivity =
                        Intent(
                            this@RegisterActivity,
                            LoginActivity::class.java
                        )
                    startActivity(intentToLoginActivity)
                }
            }

            spannableString.setSpan(clickableSpan, 25, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textViewRedirectLogin.text = spannableString
            textViewRedirectLogin.movementMethod = LinkMovementMethod.getInstance()
        }

        setContentView(binding.root)
    }
}