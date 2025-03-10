package com.example.eventapp.ViewModelFactory



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventapp.Repository.FavoriteEventRepository
import com.example.eventapp.ViewModel.FavoriteViewModel


class FavoriteViewModelFactory(private val repository: FavoriteEventRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
