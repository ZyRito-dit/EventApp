package com.example.eventapp.Repository

import com.example.eventapp.favoriteeventdata.FavoriteEvent
import com.example.eventapp.favoriteeventdata.FavoriteEventDao
import kotlinx.coroutines.flow.Flow

class FavoriteEventRepository(private val favoriteEventDao: FavoriteEventDao) {

    val allFavorites: Flow<List<FavoriteEvent>> = favoriteEventDao.getAllFavorites()

    suspend fun addToFavorite(event: FavoriteEvent) {
        favoriteEventDao.insertFavorite(event)
    }

    suspend fun removeFromFavorite(eventId: Int) {
        favoriteEventDao.deleteFavorite(eventId)
    }

}
