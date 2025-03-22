package com.example.eventapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventapp.Repository.FavoriteEventRepository
import com.example.eventapp.favoriteeventdata.FavoriteEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteEventRepository) : ViewModel() {

    private val _favoriteEvents = MutableStateFlow<List<FavoriteEvent>>(emptyList())
    val favoriteEvents: StateFlow<List<FavoriteEvent>> = _favoriteEvents.asStateFlow()



    init {
        viewModelScope.launch {
            repository.allFavorites.collect { events ->
                _favoriteEvents.value = events
            }
        }

    }
private fun loadFavoriteEvents() {
    viewModelScope.launch {
        repository.allFavorites.collect { favorites ->
            _favoriteEvents.value = favorites
        }
    }
}
fun toggleFavorite(event: FavoriteEvent) {
    viewModelScope.launch {
        if (_favoriteEvents.value.any { it.id == event.id }) {
            repository.removeFromFavorite(event.id)
        } else {
            repository.addToFavorite(event)
        }
        loadFavoriteEvents()
    }
}
    fun addToFavorite(event: FavoriteEvent) {
        viewModelScope.launch {
            repository.addToFavorite(event)
        }
    }

    fun removeFromFavorite(eventId: Int) {
        viewModelScope.launch {
            repository.removeFromFavorite(eventId)
        }
    }
}
