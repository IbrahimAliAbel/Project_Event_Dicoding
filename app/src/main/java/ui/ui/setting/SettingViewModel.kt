package ui.ui.setting

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val _isDarkModeEnabled = MutableLiveData<Boolean>()
    val isDarkModeEnabled: LiveData<Boolean> = _isDarkModeEnabled

    init {

        val sharedPreferences = getApplication<Application>().getSharedPreferences("app_prefs", 0)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        _isDarkModeEnabled.value = isDarkMode


        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun setDarkMode(isEnabled: Boolean) {
        _isDarkModeEnabled.value = isEnabled

        val sharedPreferences = getApplication<Application>().getSharedPreferences("app_prefs", 0)
        sharedPreferences.edit().putBoolean("dark_mode", isEnabled).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (isEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}