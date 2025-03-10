package com.example.eventapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventapp.Repository.EventRepository
import com.example.eventapp.Response.ListEventsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

    private val _finishedEvents = MutableStateFlow<List<ListEventsItem>>(emptyList())
    val finishedEvents: StateFlow<List<ListEventsItem>> = _finishedEvents.asStateFlow()

    init {
        getFinishedEvents()
    }

    fun getFinishedEvents() {
        viewModelScope.launch {
            try {
                val response = repository.getFinishedEvents()
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        _finishedEvents.value = data.listEvents ?: emptyList()
                        Log.d("FinishedViewModel", "Jumlah Event: ${_finishedEvents.value.size}")
                    } ?: Log.e("FinishedViewModel", "Response body null")
                } else {
                    Log.e("FinishedViewModel", "API Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("FinishedViewModel", "Exception: ${e.localizedMessage}")
            }
        }
    }
}


