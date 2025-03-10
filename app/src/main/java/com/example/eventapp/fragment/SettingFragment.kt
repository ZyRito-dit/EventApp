package com.example.eventapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eventapp.R
import com.example.eventapp.ViewModel.SettingViewModel
import com.example.eventapp.ViewModelFactory.SettingViewModelFactory
import com.example.eventapp.datastore.SettingPreferences

class SettingFragment : Fragment() {

    private val settingViewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(SettingPreferences.getInstance(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchTheme = view.findViewById<Switch>(R.id.switch_theme)


        settingViewModel.themeSetting.observe(viewLifecycleOwner) { isDarkModeActive ->
            switchTheme.isChecked = isDarkModeActive
        }


        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}
