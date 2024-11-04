package ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dicodingg.R
import com.example.dicodingg.databinding.ActivityBottomNavigationBinding

class BottomNavigation : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)


        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )


        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_bottom_navigation)
        setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished, R.id.navigation_favorite, R.id.navigation_setting
            )


        navView.setupWithNavController(navController)
    }
}