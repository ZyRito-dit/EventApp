package com.example.eventapp.ViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.Repository.EventRepository
import com.example.eventapp.ViewModel.UpcomingViewModel

class UpcomingViewModelFactory(private val repository: EventRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UpcomingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
