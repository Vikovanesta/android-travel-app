package com.example.uts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.uts.ui.AuthActivity
import com.example.uts.ui.MainActivity

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getSharedPreferences("Auth", MODE_PRIVATE).getBoolean("isLoggedIn", false)) {
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