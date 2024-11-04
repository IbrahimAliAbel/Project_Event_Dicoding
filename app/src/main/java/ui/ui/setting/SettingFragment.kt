package ui.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.dicodingg.R
import com.example.dicodingg.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupListeners()
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.settings)
        }
    }

    private fun setupListeners() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.setDarkMode(isChecked)
            updateNightMode(isChecked)
        }
    }

    private fun observeViewModel() {
        settingViewModel.isDarkModeEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.switchDarkMode.isChecked = isEnabled
            updateNightMode(isEnabled)
        }
    }

    private fun updateNightMode(isEnabled: Boolean) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}