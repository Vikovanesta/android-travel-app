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
        const val EXTRA_BIRTHDATE = "extra_birthdate"
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
                intentToLoginActivity.putExtra(EXTRA_BIRTHDATE, birthdate)

                // Validation
                var validated = true

                if (username.isEmpty()) {
                    editTextUsername.error = "Username tidak boleh kosong"
                    validated = false
                }

                if (email.isEmpty()) {
                    editTextEmail.error = "Email tidak boleh kosong"
                    validated = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.error = "Format email tidak valid"
                    validated = false
                }
                if (birthdate.isEmpty()) {
                    editTextBirthdate.error = "Tanggal lahir tidak boleh kosong"
                    validated = false
                }
                else if (birthdate.length != 10 ||
                    !birthdate.contains("/") ||
                    birthdate[2] != '/' ||
                    birthdate[5] != '/')
                {
                    editTextBirthdate.error = "Format tanggal lahir tidak valid"
                    validated = false
                }
                else if (birthdate.substring(0, 2).toInt() > 31 ||
                    birthdate.substring(0, 2).toInt() < 1 ||
                    birthdate.substring(3, 5).toInt() > 12 ||
                    birthdate.substring(3, 5).toInt() < 1 ||
                    birthdate.substring(6, 10).toInt() < 1900)
                {
                    editTextBirthdate.error = "Tanggal lahir tidak valid"
                    validated = false
                }
                else if (currentYear - birthdate.substring(6, 10).toInt() < 15)
                {
                    editTextBirthdate.error = "Anda belum cukup umur untuk mendaftar"
                    validated = false
                }

                if (password.isEmpty()) {
                    editTextPassword.error = "Password tidak boleh kosong"
                    validated = false
                } else if (password.length < 8) {
                    editTextPassword.error = "Password minimal 8 karakter"
                    validated = false
                }

                if (!checkBox.isChecked) {
                    checkBox.error = "Anda harus menyetujui syarat dan ketentuan"
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
        val selectedDate: String

        if (p3 < 10 && p2 < 9) {
            selectedDate = "0$p3/0${p2 + 1}/$p1"
        } else if (p3 < 10) {
            selectedDate = "0$p3/${p2 + 1}/$p1"
        } else if (p2 < 9) {
            selectedDate = "$p3/0${p2 + 1}/$p1"
        } else {
            selectedDate = "$p3/${p2 + 1}/$p1"
        }
        binding.editTextBirthdate.setText(selectedDate)
    }
}