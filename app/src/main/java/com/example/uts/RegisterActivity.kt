package com.example.uts

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import com.example.uts.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener
{

    private lateinit var binding: ActivityRegisterBinding

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_PASSWORD = "extra_password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        with(binding) {

            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)

            // Show date picker
            btnShowCalendar.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "date picker")
            }

            // Go to login activity
            btnRegister.setOnClickListener {
                val intentToLoginActivity =
                    Intent(
                        this@RegisterActivity,
                        LoginActivity::class.java
                    )
                // Pass data to login activity
                val username = editTextUsername.text.toString()
                val email = editTextEmail.text.toString()
                val birthdate = editTextBirthdate.text.toString()
                val password = editTextPassword.text.toString()

                intentToLoginActivity.putExtra(EXTRA_USERNAME, username)
                intentToLoginActivity.putExtra(EXTRA_EMAIL, email)
                intentToLoginActivity.putExtra(EXTRA_PASSWORD, password)

                // Validation
                var validated = true

                if (username.isEmpty()) {
                    editTextUsername.error = "Username is required"
                    validated = false
                }

                if (email.isEmpty()) {
                    editTextEmail.error = "Email is required"
                    validated = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.error = "Invalid email format"
                    validated = false
                }
                if (birthdate.isEmpty()) {
                    editTextBirthdate.error = "Birthdate is required"
                    validated = false
                }
                else if (birthdate.length != 10 ||
                    !birthdate.contains("/") ||
                    birthdate[2] != '/' ||
                    birthdate[5] != '/')
                {
                    editTextBirthdate.error = "Invalid birthdate format"
                    validated = false
                }
                else if (birthdate.substring(0, 2).toInt() > 31 ||
                    birthdate.substring(0, 2).toInt() < 1 ||
                    birthdate.substring(3, 5).toInt() > 12 ||
                    birthdate.substring(3, 5).toInt() < 1 ||
                    birthdate.substring(6, 10).toInt() < 1900)
                {
                    editTextBirthdate.error = "Invalid birthdate"
                    validated = false
                }
                else if (currentYear - birthdate.substring(6, 10).toInt() < 15)
                {
                    editTextBirthdate.error = "You must be at least 15 years old"
                    validated = false
                }

                if (password.isEmpty()) {
                    editTextPassword.error = "Password is required"
                    validated = false
                } else if (password.length < 8) {
                    editTextPassword.error = "Password must be at least 8 characters long"
                    validated = false
                }

                if (validated) {
                    startActivity(intentToLoginActivity)
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

    override fun onDateSet(p0: android.widget.DatePicker?, p1: Int, p2: Int, p3: Int) {

        val selectedDate: String =
            if (p3 < 10 && p2 < 9) {
                "0$p3/0${p2 + 1}/$p1"
            } else if (p3 < 10) {
                "0$p3/${p2 + 1}/$p1"
            } else if (p2 < 9) {
                "$p3/0${p2 + 1}/$p1"
            } else {
                "$p3/${p2 + 1}/$p1"
            }
        binding.editTextBirthdate.setText(selectedDate)
    }
}