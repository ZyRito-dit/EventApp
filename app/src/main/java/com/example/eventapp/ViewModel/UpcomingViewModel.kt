package com.example.eventapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventapp.Repository.EventRepository
import com.example.eventapp.Response.ListEventsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log

class UpcomingViewModel(private val repository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableStateFlow<List<ListEventsItem>>(emptyList())
    val upcomingEvents: StateFlow<List<ListEventsItem>> = _upcomingEvents.asStateFlow()

    fun getUpcomingEvents() {
        viewModelScope.launch {
            try {
                val response = repository.getUpcomingEvents()
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents ?: emptyList()
                    _upcomingEvents.value = events
                    Log.d("UpcomingViewModel", "Data API: ${events.size} event(s) diterima")
                } else {
                    Log.e("UpcomingViewModel", "Gagal mengambil data: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("UpcomingViewModel", "Exception: ${e.message}", e)
            }
        }
    }
}