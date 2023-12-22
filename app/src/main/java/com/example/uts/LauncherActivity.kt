package com.example.uts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.uts.ui.AuthActivity
import com.example.uts.ui.MainActivity
import com.example.uts.utils.SessionManager

class LauncherActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager.getInstance(this)

        if (sessionManager.isLoggedIn()) {
            Log.d("LauncherActivity", "onCreate: isLoggedIn")
            startActivity(android.content.Intent(this, MainActivity::class.java))
            this.finish()
        } else {
            Log.d("LauncherActivity", "onCreate: isNotLoggedIn")
            startActivity(android.content.Intent(this, AuthActivity::class.java))
            this.finish()
        }
    }
}