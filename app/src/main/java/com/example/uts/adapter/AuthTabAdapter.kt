package com.example.uts.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uts.ui.fragments.LoginFragment
import com.example.uts.ui.fragments.RegisterFragment

class AuthTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val page = arrayOf(
        LoginFragment(),
        RegisterFragment(),
    )

    override fun getItemCount(): Int {
        return page.size
    }

    override fun createFragment(position: Int): Fragment {
        return page[position]
    }
}