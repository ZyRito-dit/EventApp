package com.example.eventapp.Repository
import com.example.eventapp.Response.ResponseGetEvent
import com.example.eventapp.Retrofit.ApiService
import retrofit2.Response

class EventRepository(private val apiService: ApiService) {

    suspend fun getUpcomingEvents(): Response<ResponseGetEvent> {
        return apiService.getUpcomingEvents()
    }
    suspend fun getFinishedEvents(): Response<ResponseGetEvent> {
        return apiService.getFinishedEvents()
    }

}
