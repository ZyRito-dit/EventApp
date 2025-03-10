package com.example.eventapp.favoriteeventdata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(event: FavoriteEvent)

    @Query("SELECT * FROM favorite_events")
    fun getAllFavorites(): Flow<List<FavoriteEvent>>

    @Query("DELETE FROM favorite_events WHERE id = :eventId")
    suspend fun deleteFavorite(eventId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_events WHERE id = :eventId)")
    fun isFavorite(eventId: Int): Flow<Boolean>
}
