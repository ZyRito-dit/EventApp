package com.example.eventapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eventapp.R
import com.example.eventapp.ViewModel.SettingViewModel
import com.example.eventapp.ViewModelFactory.SettingViewModelFactory
import com.example.eventapp.datastore.SettingPreferences
import com.example.eventapp.utils.ReminderHelper

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
        val switchTheme = view.findViewById<SwitchCompat>(R.id.switch_theme)
        val switchReminder = view.findViewById<SwitchCompat>(R.id.switch_daily_reminder)
        settingViewModel.themeSetting.observe(viewLifecycleOwner) { isDarkModeActive ->
            switchTheme.isChecked = isDarkModeActive
        }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSetting(isChecked)
        }
        settingViewModel.reminderSetting.observe(viewLifecycleOwner) { isReminderActive ->
            switchReminder.isChecked = isReminderActive
        }
        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveReminderSetting(isChecked)
            if (isChecked) {

                val eventDate = getEventDateFromAPI()

                if (!eventDate.isNullOrEmpty()) {
                    ReminderHelper.scheduleDailyReminder(requireContext(), eventDate)
                } else {
                    Log.e("SettingFragment", "Event date tidak ditemukan! Tidak bisa menjadwalkan pengingat.")
                }
            } else {
                ReminderHelper.cancelDailyReminder(requireContext())
            }
        }
    }
    private fun getEventDateFromAPI(): String? {

        return "2025-04-10"
    }
}

