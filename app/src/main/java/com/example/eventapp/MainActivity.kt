package com.example.eventapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.eventapp.ViewModel.SettingViewModel
import com.example.eventapp.ViewModelFactory.SettingViewModelFactory
import com.example.eventapp.datastore.SettingPreferences
import com.example.eventapp.fragment.FavoriteFragment
import com.example.eventapp.fragment.FinishedFragment
import com.example.eventapp.fragment.SettingFragment
import com.example.eventapp.fragment.Upcoming
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(SettingPreferences.getInstance(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        settingViewModel.themeSetting.observe(this) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        bottomNavigation.setOnItemSelectedListener { menuItem ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            val newFragment = when (menuItem.itemId) {
                R.id.nav_upcoming -> Upcoming()
                R.id.nav_finished -> FinishedFragment()
                R.id.nav_favorite -> FavoriteFragment()
                R.id.nav_setting -> SettingFragment()
                else -> null
            }

            if (newFragment != null && currentFragment?.javaClass != newFragment.javaClass) {
                loadFragment(newFragment)
            }
            true
        }


        if (savedInstanceState == null) {
            loadFragment(Upcoming())
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
